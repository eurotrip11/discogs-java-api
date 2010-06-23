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


public abstract class ListResults {
    
    private int offset;

    private int pageSize;

    private int count;

    public static final int DEFAULT_PAGE_NUM = 1;

    public static final int DEFAULT_PAGE_SIZE = 20;



    public ListResults(int pageNumber, int pageSize, int totalCount) {
        this.count = totalCount;
        if (pageSize < 1)
            this.pageSize = 2;
        else
            this.pageSize = pageSize;
        if (pageNumber < 1)
            this.offset = 1;
        else if (pageNumber > getMaxPageNumber())
            this.offset = getMaxPageNumber();
        else
            this.offset = pageNumber;
    }


    public int getFirstPageNumber() {
        return 1;
    }

    public int getMaxPageNumber() {
        if (getCount() < getPageSize()) {
            return 1;
        } else {
            int remainder = getCount() % getPageSize();
            int quotient = getCount() / getPageSize();
            return remainder != 0 ? quotient + 1 : quotient;
        }
    }

    public int getNextPageNumber() {
        if (getPageNumber() == getMaxPageNumber())
            return getMaxPageNumber();
        else
            return getPageNumber() + 1;
    }

    public int getPreviousPageNumber() {
        if (getPageNumber() == 1)
            return getPageNumber();
        else
            return getPageNumber() - 1;
    }

    public int getPageNumber() {
        return offset;
    }

    public int getPageSize() {
        return pageSize;
    }

    public int getCount() {
        return count;
    }
}
