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
import com.discogs.api.model.Artist;
import com.discogs.api.model.Label;
import com.discogs.api.model.Release;
import com.discogs.api.model.Track;
import com.discogs.api.webservice.filter.ArtistFilter;
import com.discogs.api.webservice.filter.LabelFilter;
import com.discogs.api.webservice.filter.ReleaseFilter;
import com.discogs.api.webservice.impl.HttpClientWebService;
import com.discogs.api.webservice.results.ArtistResult;
import com.discogs.api.webservice.results.ArtistSearchResults;
import com.discogs.api.webservice.results.LabelResult;
import com.discogs.api.webservice.results.LabelSearchResults;
import com.discogs.api.webservice.results.ReleaseResult;
import java.util.List;
import static org.testng.AssertJUnit.assertEquals;
import static org.testng.AssertJUnit.assertNotNull;
import static org.testng.AssertJUnit.assertTrue;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Integration tests using the {@link Query} class.
 * 
 * @author Patrick Ruhkopf
 * @see Query
 */
public class QueryTest {

    private Logger logger = LoggerFactory.getLogger(QueryTest.class);
    /**
     * Enter valid credentials here:
     */
    private static final String MY_USERNAME = "your_username";
    private static final String MY_PASSWORD = "your_password";
    /**
     * A default {@link Query}
     */
    Query q;
    /**
     * A {@link Query} with Authentication information
     */
    Query authQ;

    @BeforeClass
    public void setUp() {
        this.q = new Query();

        // if you want to test the functions that require authentication,
        // edit the username and passwort constants!
        HttpClient client = new HttpClient();
        client.getState().setCredentials(
                new AuthScope("discogs.com", 80, AuthScope.ANY_REALM),
                new UsernamePasswordCredentials(MY_USERNAME, MY_PASSWORD));
        this.authQ = new Query(new HttpClientWebService(client));

    }

    @BeforeMethod
    public void wait1Second() {
        // because we are only allowed to query the web service once in a second
        try {
            logger.debug("waiting 1 second to avoid too much server load");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            logger.warn(e.getMessage(), e);
        }
    }

    @Test
    public void findArtist() throws JDiscogsException {
        // Search for all artists matching the given name.
        ArtistFilter f = new ArtistFilter("Doors Down");

        ArtistSearchResults artistResults = q.findArtists(f);
        logger.debug("[maxPageNumber] "+artistResults.getMaxPageNumber());
        logger.debug("[numberPage] "+artistResults.getPageNumber());
        // some tests
        assertTrue(artistResults.getArtistResults().size() > 0);
        boolean found = false;
        for (ArtistResult ar : artistResults.getArtistResults()) {
            Artist artist = ar.getArtist();
            logger.debug("[artist] "+ artist.getName() +" [exact] " + ar.getScore());
            assertNotNull(artist.getName());
            if ("3 Doors Down".equals(artist.getName())) {
                found = true;
            }
        }
        assertTrue(found);
    }

    @Test
    public void getArtist() throws JDiscogsException {
        // 3 doors down id
        String name = "3 Doors Down";
        Artist artist = q.getArtist(name);
        assertNotNull(artist);
        logger.debug(artist.toString());

        // some tests
        assertEquals("3 Doors Down", artist.getName());
        assertNotNull(artist.getReleases());
        assertNotNull(artist.getReleases().getRelease());
        assertTrue(artist.getReleases().getRelease().size() > 0);

        for (Release r : artist.getReleases().getRelease()) {
            logger.debug(r.toString());
        }
    }

    @Test
    public void findRelease() throws JDiscogsException {
        // Search for a specific release
        ReleaseFilter f = new ReleaseFilter();
        f.setArtistName("3 Doors Down");
        f.setTitle("Away from the Sun");
        ReleaseSearchResults releaseResults = q.findReleases(f);

        // some tests
        assertTrue(releaseResults.getReleaseResults().size() > 0);
        ReleaseResult rs = releaseResults.getReleaseResults().get(0);
        assertNotNull(rs);

        Release r = rs.getRelease();
        logger.debug("[release] " + r.getId() + " - " + r.getTitle());
        assertNotNull(r.getId());
        assertNotNull(r.getTitle());
    }
    
    @Test
    public void getRelease() throws JDiscogsException {
        // Away from the Sun from 3 Doors Down id
        String id = "593472";

        Release r = q.getRelease(id);
        logger.debug(r.toString());

        // some tests
        assertTrue("away from the sun".equalsIgnoreCase(r.getTitle()));
        assertNotNull(r.getArtists().getArtist());
        assertTrue("3 doors down".equalsIgnoreCase(r.getArtists().getArtist().getName()));
        assertNotNull(r.getTracklist());
        assertTrue(r.getTracklist().getTrack().size() > 10);

        // print tracks
        for (Track t : r.getTracklist().getTrack()) {
            logger.debug(t.toString());
        }
    }

    @Test
    public void findLabel() throws JDiscogsException {
        LabelFilter labelFilter = new LabelFilter();
        labelFilter.setName("Universal Records");
        LabelSearchResults labelSearchResults = q.findLabels(labelFilter);

        assertTrue(labelSearchResults.getLabelResults().size() > 0);
         for (LabelResult labelResult : labelSearchResults.getLabelResults()) {
             Label label = labelResult.getLabel();
             logger.debug("[label] " + label.getName() + " [exact] " + labelResult.getScore());
             assertNotNull(label.getName());
         }
    }

    @Test
    public void getLabel() throws JDiscogsException {
        String labelName = "Moda Records";
        Label label = q.getLabel(labelName);
        logger.debug(label.toString());
        assertNotNull(label.getContent());
        List<Object> content = label.getContent();
        for (Object object : content) {
            logger.debug(object.toString());
        }
    }

}
