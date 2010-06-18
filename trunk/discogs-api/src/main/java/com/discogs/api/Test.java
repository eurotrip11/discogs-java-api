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

import com.discogs.api.exception.JDiscogsException;
import com.discogs.api.model.Artist;
import com.discogs.api.model.Label;
import com.discogs.api.model.Release;
import com.discogs.api.webservice.impl.HttpClientWebService;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;


public class Test {

    public static void main(String[] args) {
        try {
            Query query = new Query();
            /*Artist artist = query.getArtist("Aphex Twin");
            System.out.println(artist.getName());
            Release release = query.getRelease("1");
            System.out.println(release.getTitle());
            Label label = query.getLabel("Svek");
            System.out.println(label.getName());*/
            HttpClient client = new HttpClient();
            HttpClientWebService ser =  (HttpClientWebService) query.getWebService();
//            HostConfiguration configuration = new HostConfiguration();
//            configuration.setProxy("218.248.44.216", 8080);
//            client.setHostConfiguration(configuration);
            ser.setHttpClient(client);
            List<Artist> artists = query.findArtists("Vasco Rossi");
            for (Artist artist : artists) {
                System.out.println("artist:" + artist.getName());
                System.out.println("artist:" + artist.getRole());
            }
            List<Release> releases = query.findReleases("Vasco Rossi Stupido Hotel");
            for (Release release : releases) {
                System.out.println("release:" + release.getTitle());
                System.out.println("release:" + release.getCountry());
                System.out.println("release:" + release.getReleased());
                System.out.println("release:" + release.getLabel());
                System.out.println("release:" + release.getLabels().getLabel().getName());
            }
            List<Label> labels = query.findLabels("EMI Music (Italy)");
            for (Label label : labels) {
                List<Object> objects = label.getContent();
                for (Object object : objects) {
                     System.out.println("labels: "+object.toString());
                }
            }
        } catch (JDiscogsException ex) {
            ex.printStackTrace();
        }
    }
}
