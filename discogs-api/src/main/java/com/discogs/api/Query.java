/*
 * Discogs Java Api 1.0-SNAPSHOT
 * Copyright (C) 2010 Giuseppe Trisciuoglio
 *
 * http://code.google.com/p/discogs-java-api/
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.discogs.api;

import com.discogs.api.webservice.results.ReleaseSearchResults;
import com.discogs.api.exception.JDiscogsException;
import com.discogs.api.exception.WebServiceException;
import com.discogs.api.model.Artist;
import com.discogs.api.model.Exactresults;
import com.discogs.api.model.Label;
import com.discogs.api.model.Release;
import com.discogs.api.model.Resp;
import com.discogs.api.model.Result;
import com.discogs.api.model.Searchresults;
import com.discogs.api.webservice.WebService;
import com.discogs.api.webservice.filter.ArtistFilter;
import com.discogs.api.webservice.filter.ReleaseFilter;
import com.discogs.api.webservice.impl.HttpClientWebService;
import com.discogs.api.webservice.results.ArtistSearchResults;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Query {

    private WebService webService;
    private Logger logger = LoggerFactory.getLogger(Query.class);
    public static final String RELEASE_TYPE = "releases";
    public static final String ARTIST_TYPE = "artists";
    public static final String LABEL_TYPE = "labels";
    public static final String CATNO = "catno";
    public static final String ALL_TYPE = "all";

    public Query() {
        webService = new HttpClientWebService();
    }

    public Query(WebService webService) {
        this.webService = webService;
    }

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public Artist getArtist(String name) throws JDiscogsException {
        Artist artist = null;
        try {
            Resp resp = getWebService().get("artist", null, new String[]{name});
            artist = resp.getArtist();
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return artist;
    }

    public Release getRelease(String id) throws JDiscogsException {
        Release release = null;
        try {
            Resp resp = getWebService().get("release", null, new String[]{id});
            release = resp.getRelease();
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return release;
    }

    public Label getLabel(String name) throws JDiscogsException {
        Label label = null;
        try {
            Resp resp = getWebService().get("label", null, new String[]{name});
            label = resp.getLabel();
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return label;
    }

    public ArtistSearchResults findArtists(ArtistFilter artistFilter) throws JDiscogsException {
        return null;
    }

    private List<Artist> findArtists(String name) throws JDiscogsException {
        List<Artist> artists = new ArrayList<Artist>();
        List<Result> results = new ArrayList<Result>();
        try {
            Resp resp = getWebService().get(null, ARTIST_TYPE, new String[]{name});
            Searchresults searchresults = resp.getSearchresults();
            Exactresults exactresults = resp.getExactresults();
            if (exactresults != null && exactresults.getResult() != null){
                results.addAll(exactresults.getResult());
            }
            if (exactresults == null && searchresults.getResult() != null){
                 results.addAll(searchresults.getResult());
            }
            for (Result result : results) {
                if (isArtist(result)) {
                    artists.add(getArtist(result.getTitle()));
                }
            }
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return artists;
    }

    public ReleaseSearchResults findReleases(ReleaseFilter releaseFilter) throws JDiscogsException {
        return null;
    }

    private List<Release> findReleases(String artist, String title) throws JDiscogsException {
        return findReleases(artist.concat(" ").concat(title));
    }
    private List<Release> findReleases(String name) throws JDiscogsException {
        List<Release> releases = new ArrayList<Release>();
        List<Result> results = new ArrayList<Result>();
        try {
            Resp resp = getWebService().get(null, RELEASE_TYPE, new String[]{name});
            Searchresults searchresults = resp.getSearchresults();
            Exactresults exactresults = resp.getExactresults();
            if (exactresults != null
                    && exactresults.getResult() != null){
                results.addAll(exactresults.getResult());
            }
            if (exactresults == null && searchresults.getResult() != null){
                 results.addAll(searchresults.getResult());
            }
            for (Result result : results) {
                if (isRelease(result)) {
                    String uri = result.getUri();
                    releases.add(getRelease(uri.substring(uri.lastIndexOf("/") + 1, uri.length())));
                }
            }
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return releases;
    }

    public List<Label> findLabels(String name) throws JDiscogsException {
        List<Label> labels = new ArrayList<Label>();
        List<Result> results = new ArrayList<Result>();
        try {
            Resp resp = getWebService().get(null, LABEL_TYPE, new String[]{name});
            Searchresults searchresults = resp.getSearchresults();
            Exactresults exactresults = resp.getExactresults();
            if (exactresults != null && exactresults.getResult() != null){
                results.addAll(exactresults.getResult());
            }
            if (exactresults == null && searchresults.getResult() != null){
                 results.addAll(searchresults.getResult());
            }
            for (Result result : results) {
                if (isLabel(result)){
                    labels.add(getLabel(result.getTitle()));
                }
            }
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return labels;
    }

    private boolean isArtist(Result result) {
        if (result == null
                || result.getType() == null
                || result.getType().trim().equalsIgnoreCase("")
                || !result.getType().equalsIgnoreCase("artist")) {
            return false;
        }
        return true;
    }

    private boolean isRelease(Result result) {
        if (result == null
                || result.getType() == null
                || result.getType().trim().equalsIgnoreCase("")
                || !result.getType().equalsIgnoreCase("release")) {
            return false;
        }
        return true;
    }

    private boolean isLabel(Result result) {
        if (result == null
                || result.getType() == null
                || result.getType().trim().equalsIgnoreCase("")
                || !result.getType().equalsIgnoreCase("label")) {
            return false;
        }
        return true;
    }
}
