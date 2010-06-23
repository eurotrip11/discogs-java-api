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

public class ReleaseSearchResults extends ListResults {

    private List<ReleaseResult> releaseResults = new ArrayList<ReleaseResult>();

    private long exactCount;

    public ReleaseSearchResults(int pageNumber, int totalCount)
    {
        super(pageNumber, ListResults.DEFAULT_PAGE_SIZE, totalCount);
    }

    public long getExactCount() {
        return exactCount;
    }

    public void setExactCount(long exactCount) {
        this.exactCount = exactCount;
    }

    public List<ReleaseResult> getReleaseResults() {
        return releaseResults;
    }

    public void setReleaseResults(List<ReleaseResult> releaseResults) {
        this.releaseResults = releaseResults;
    }

    public boolean addReleaseResult(ReleaseResult releaseResult){
        if (releaseResults == null){
            releaseResults = new ArrayList<ReleaseResult>();
        }
        return releaseResults.add(releaseResult);
    }
}
