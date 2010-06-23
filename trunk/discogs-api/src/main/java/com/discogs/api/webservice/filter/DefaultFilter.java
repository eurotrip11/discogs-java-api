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

import java.util.HashMap;
import java.util.Map;

public abstract class DefaultFilter implements Filter {

    private int limit;
    private int pageNumber;

    public DefaultFilter() {
    }

    public DefaultFilter(int limit, int pageNumber) {
        this.limit = limit;
    }

    public long getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public Map<String, String> createParams() {
        Map<String, String> map = new HashMap<String, String>();
        if (getPageNumber() > 0){
            map.put("page", Integer.toString(getPageNumber()));
        }
        return map;
    }
}
