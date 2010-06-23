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
import java.util.HashMap;
import java.util.Map;

public class ReleaseFilter extends DefaultFilter {

    private String artistName;
    private String title;
    private String[] releaseTypes;

    public ReleaseFilter() {
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public String[] getReleaseTypes() {
        return releaseTypes;
    }

    public void setReleaseTypes(String[] releaseTypes) {
        this.releaseTypes = releaseTypes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public Map<String, String> createParams() {
        Map<String, String> params = super.createParams();
        Map<String, String> paramsLocal = new HashMap<String, String>();
        if (!StringUtilities.isBlank(getTitle())) {
            paramsLocal.put("q", StringUtilities.replaceWhiteSpace(getTitle()));
        }
        if (!StringUtilities.isBlank(getArtistName())) {
            if (paramsLocal.containsKey("q")){
                String param = paramsLocal.get("q");
                paramsLocal.put("q", StringUtilities.replaceWhiteSpace(param.concat(" ").concat(getArtistName())));
            }
            else
                paramsLocal.put("q", StringUtilities.replaceWhiteSpace(getArtistName()));
        }
        if (!params.containsKey("q") && paramsLocal.isEmpty()) {
            throw new IllegalArgumentException("This filter must specify an artist name or title!");
        }
        params.putAll(paramsLocal);
        return params;
    }

}
