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

public class SearchResult {

    private Integer score;

    private Long offset;

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Long getOffset() {
        return offset;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

}
