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
package com.discogs.api.webservice.filter;

import com.discogs.api.utilities.StringUtilities;
import java.util.Map;

public class ArtistFilter extends DefaultFilter {

    private String artistName;

    public ArtistFilter(String artistName) {
        this.artistName = artistName;
    }

    public ArtistFilter(String artistName, int limit, int pageNumber) {
        super(limit, pageNumber);
        this.artistName = artistName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    @Override
    public Map<String, String> createParams() {
        Map<String, String> params = super.createParams();
        if (!StringUtilities.isBlank(getArtistName())) {
            params.put("q", StringUtilities.replaceWhiteSpace(getArtistName()));
        } else {
            if (!params.containsKey("q")) {
                throw new IllegalArgumentException("This filter must specify an artist name!");
            }
        }
        return params;
    }
}
