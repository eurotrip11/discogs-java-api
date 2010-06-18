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

package com.discogs.api.webservice;

import com.discogs.api.exception.WebServiceException;
import com.discogs.api.model.Resp;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultWebService implements WebService {

    private Logger logger = LoggerFactory.getLogger(DefaultWebService.class);
    public static final String URL_ENCODING = "UTF-8";
    private static final String API_KEY_PROP = "apiKey";
    private String host = "www.discogs.com";
    private String protocol = "http";
    private Integer port = null;

    protected String makeURL(String entity, String type, String[] params) throws WebServiceException {
        StringBuffer url = new StringBuffer();
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("f", "xml");
        urlParams.put("api_key", getApiKey());
        url.append(this.protocol).append("://").append(this.host);
        if (this.port != null) {
            url.append(":").append(this.port);
        }
        if (entity == null || entity.trim().length() == 0) {
            url.append("/search");
            urlParams.put("type", type == null ? "all" : type);
            StringBuffer param = new StringBuffer();
            for (int i = 0; i < params.length; i++) {
                String string = params[i];
                param.append(string.replace(' ', '+'));
                if (i < params.length - 1) {
                    param.append('+');
                }
            }
            urlParams.put("q", param.toString());
        } else {
            url.append("/").append(entity).append("/");
            for (int i = 0; i < params.length; i++) {
                String string = params[i];
                url.append(string.replace(' ', '+'));
                if (i < params.length - 1) {
                    url.append('+');
                }
            }
        }
        url.append("?");
        Iterator<Map.Entry<String, String>> it = urlParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            try {
                url.append(e.getKey()).append("=").append(URLEncoder.encode(e.getValue(), URL_ENCODING)).append("&");
            } catch (UnsupportedEncodingException ex) {
                logger.error("Internal Error: Could not encode url parameter " + e.getKey(), ex);
            }
        }
        return url.substring(0, url.length() - 1);
    }

    public Resp get(String entity, String type, String[] params) throws WebServiceException {
        String url = this.makeURL(entity, type, params);
        logger.debug("GET " + url);
        return doGet(url);
    }

    public void post() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public abstract Resp doGet(String url) throws WebServiceException;

    public abstract void doPost(String url) throws WebServiceException;

    public String getApiKey() throws WebServiceException {
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("discogs-api.properties");
            Properties properties = new Properties();
            properties.load(is);
            return properties.getProperty(API_KEY_PROP);
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new WebServiceException("Api Key not found. ", e);
        }
    }
}
