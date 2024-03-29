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

package com.discogs.api.webservice.results;

import java.util.ArrayList;
import java.util.List;


public class ArtistSearchResults extends ListResults {

    private List<ArtistResult> artists = new ArrayList<ArtistResult>();

    private long exactCount;

    public ArtistSearchResults(int pageNumber, int totalCount)
    {
        super(pageNumber, ListResults.DEFAULT_PAGE_SIZE, totalCount);
    }

    public long getExactCount() {
        return exactCount;
    }

    public void setExactCount(long exactCount) {
        this.exactCount = exactCount;
    }

    public List<ArtistResult> getArtistResults() {
        return artists;
    }

    public void setArtistResults(List<ArtistResult> artists) {
        this.artists = artists;
    }

    public boolean addArtistResult(ArtistResult artist){
        if (artists == null){
            artists = new ArrayList<ArtistResult>();
        }
        return artists.add(artist);
    }
}
