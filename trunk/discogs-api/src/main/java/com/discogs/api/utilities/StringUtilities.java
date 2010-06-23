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



package com.discogs.api.utilities;


public final class StringUtilities {

    public static boolean isBlank(String value){
        if (value == null || value.trim().equals("")){
            return true;
        }
        return false;
    }

    public static String replaceWhiteSpace(String value){
        if (!isBlank(value)){
            return value.trim().replaceAll(" ", "+");
        }
        return null;
    }

}
