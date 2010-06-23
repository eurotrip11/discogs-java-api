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

import com.discogs.api.common.DiscogsCostants;
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
import com.discogs.api.webservice.filter.LabelFilter;
import com.discogs.api.webservice.filter.ReleaseFilter;
import com.discogs.api.webservice.impl.HttpClientWebService;
import com.discogs.api.webservice.includes.ArtistIncludes;
import com.discogs.api.webservice.includes.LabelIncludes;
import com.discogs.api.webservice.includes.ReleaseIncludes;
import com.discogs.api.webservice.results.ArtistResult;
import com.discogs.api.webservice.results.ArtistSearchResults;
import com.discogs.api.webservice.results.LabelResult;
import com.discogs.api.webservice.results.LabelSearchResults;
import com.discogs.api.webservice.results.ReleaseResult;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Query {

    private WebService webService;
    private Logger logger = LoggerFactory.getLogger(Query.class);
    public static final String RELEASE_TYPE = "releases";
    public static final String ARTIST_TYPE = "artists";
    public static final String LABEL_TYPE = "labels";
    
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
        return getArtist(name, null);
    }

    private Artist getArtist(String name, ArtistIncludes artistIncludes) throws JDiscogsException {
        Artist artist = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("q", name);
            Resp resp = getWebService().get("artist", null, params);
            artist = resp.getArtist();
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return artist;
    }
    
    public Release getRelease(String id) throws JDiscogsException {
        return getRelease(id, null);
    }
    
    private Release getRelease(String id, ReleaseIncludes releaseIncludes) throws JDiscogsException {
        Release release = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("q", id);
            Resp resp = getWebService().get("release", null, params);
            release = resp.getRelease();
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return release;
    }
    public Label getLabel(String name) throws JDiscogsException {
        return getLabel(name, null);
    }
    
    private Label getLabel(String name, LabelIncludes labelIncludes) throws JDiscogsException {
        Label label = null;
        try {
            Map<String, String> params = new HashMap<String, String>();
            params.put("q", name);
            Resp resp = getWebService().get("label", null, params);
            label = resp.getLabel();
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return label;
    }

    public ArtistSearchResults findArtists(ArtistFilter artistFilter) throws JDiscogsException {
        ArtistSearchResults artistSearchResults = null;
        try {
            Resp resp = getWebService().get(null, ARTIST_TYPE, artistFilter.createParams());
            Searchresults searchResults = resp.getSearchresults();
            int count = 0;
            if (searchResults != null && searchResults.getNumResults() != null){
                count = searchResults.getNumResults().intValue();
            }
            int pageNumber = artistFilter.getPageNumber();
            artistSearchResults = new ArtistSearchResults(pageNumber, count);
            Exactresults exactResults = resp.getExactresults();
            if (exactResults != null && exactResults.getResult() != null) {
                List<Result> results = exactResults.getResult();
                artistSearchResults.setExactCount(results.size());
                for (Result result : results) {
                    ArtistResult artistResult = new ArtistResult();
                    artistResult.setOffset(result.getNum().longValue());
                    artistResult.setScore(DiscogsCostants.SCORE_EXACT);
                    artistResult.setArtist(new Artist());
                    artistResult.getArtist().setName(result.getTitle());
                    artistSearchResults.addArtistResult(artistResult);
                }
            }
            if (searchResults != null && searchResults.getResult() != null) {
                List<Result> results = searchResults.getResult();
                int i =1;
                for (Result result : results) {
                    ArtistResult artistResult = new ArtistResult();
                    artistResult.setOffset(result.getNum().longValue());
                    //TODO score discogs no know
                    artistResult.setScore(0);
                    artistResult.setArtist(new Artist());
                    artistResult.getArtist().setName(result.getTitle());
                    artistSearchResults.addArtistResult(artistResult);
                    i++;
                }
            }

        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return artistSearchResults;
    }

    public ReleaseSearchResults findReleases(ReleaseFilter releaseFilter) throws JDiscogsException {
        ReleaseSearchResults releaseSearchResults = null;
        try {
            Resp resp = getWebService().get(null, RELEASE_TYPE, releaseFilter.createParams());
            int count = 0;
            Searchresults searchResults = resp.getSearchresults();
            Exactresults exactResults = resp.getExactresults();
            if (searchResults != null && searchResults.getNumResults() != null){
                count = searchResults.getNumResults().intValue();
            }
            releaseSearchResults = new ReleaseSearchResults(releaseFilter.getPageNumber(), count);
            if (exactResults != null && exactResults.getResult() != null){
                releaseSearchResults.setExactCount(exactResults.getResult().size());
                for (Result result : exactResults.getResult()) {
                    ReleaseResult releaseResult = new ReleaseResult();
                    releaseResult.setOffset(result.getNum().longValue());
                    releaseResult.setScore(DiscogsCostants.SCORE_EXACT);
                    String uri = result.getUri();
                    Release release = new Release();
                    BigInteger id = BigInteger.valueOf(Long.valueOf(uri.substring(uri.lastIndexOf("/") + 1, uri.length())));
                    release.setId(id);
                    release.setTitle(result.getTitle());
                    releaseResult.setRelease(release);
                    releaseSearchResults.addReleaseResult(releaseResult);
                }
            }
            if (searchResults != null && searchResults.getResult() != null){
                for (Result result : searchResults.getResult()) {
                    ReleaseResult releaseResult = new ReleaseResult();
                    releaseResult.setOffset(result.getNum().longValue());
                    releaseResult.setScore(0);
                    String uri = result.getUri();
                    Release release = new Release();
                    BigInteger id = BigInteger.valueOf(Long.valueOf(uri.substring(uri.lastIndexOf("/") + 1, uri.length())));
                    release.setId(id);
                    release.setTitle(result.getTitle());
                    releaseResult.setRelease(release);
                    releaseSearchResults.addReleaseResult(releaseResult);
                }
            }
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return releaseSearchResults;
    }

    public LabelSearchResults findLabels(LabelFilter labelFilter) throws JDiscogsException {
        LabelSearchResults labelSearchResult = null;
        try {
            Resp resp = getWebService().get(null, LABEL_TYPE, labelFilter.createParams());
            Searchresults searchResults = resp.getSearchresults();
            Exactresults exactResults = resp.getExactresults();
            int count = 0;
            if (searchResults != null && searchResults.getNumResults() != null){
                count = searchResults.getNumResults().intValue();
            }
            labelSearchResult = new LabelSearchResults(labelFilter.getPageNumber(), count);
            if (exactResults != null && exactResults.getResult() != null){
                labelSearchResult.setExactCount(exactResults.getResult().size());
                for (Result result : exactResults.getResult()) {
                    LabelResult labelResult = new LabelResult();
                    labelResult.setOffset(result.getNum().longValue());
                    labelResult.setScore(DiscogsCostants.SCORE_EXACT);
                    Label label = new Label();
                    label.setName(result.getTitle());
                    labelResult.setLabel(label);
                    labelSearchResult.addLabelResult(labelResult);
                }
            }
            if (searchResults != null && searchResults.getResult() != null) {
                for (Result result : searchResults.getResult()) {
                    LabelResult labelResult = new LabelResult();
                    labelResult.setOffset(result.getNum().longValue());
                    labelResult.setScore(0);
                    Label label = new Label();
                    label.setName(result.getTitle());
                    labelResult.setLabel(label);
                    labelSearchResult.addLabelResult(labelResult);
                }
            }
        } catch (WebServiceException e) {
            logger.error(e.getMessage());
            throw new JDiscogsException(e.getMessage(), e);
        }
        return labelSearchResult;
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
