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
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class DefaultWebService implements WebService {

    private Logger logger = LoggerFactory.getLogger(DefaultWebService.class);
    
    private static final String API_KEY_PROP        = "apiKey";
    private static final String HOST_KEY_PROP       = "hostName";
    private static final String PROTOCOL_KEY_PROP   = "protocol";
    private static final String PORT_KEY_PROP       = "port";
    private static final String ENABLE_URL_ENCODING_KEY_PROP = "enableURLEncoding";
    private static final String URL_ENCODING_KEY_PROP = "ecodingURL";

    private static final String DEFAULT_HOST = "www.discogs.com";
    private static final String DEFAULT_PROTOCOL = "http";
    private static final Integer DEFAULT_PORT = null;
    private static final Boolean DEFAULT_URL_ENCODING_ENABLED = false;
    public static final String   DEFAULT_URL_ENCODING = "UTF-8";

    protected String makeURL(String entity, String type, Map<String, String> params) throws WebServiceException {
        StringBuffer url = new StringBuffer();
        Map<String, String> urlParams = new HashMap<String, String>();
        urlParams.put("f", "xml");
        urlParams.put("api_key", getApiKey());
        url.append(getProtocol()).append("://").append(getHostname());
        if (getPort() != null) {
            url.append(":").append(getPort());
        }
        if (entity == null || entity.trim().length() == 0) {
            url.append("/search");
            urlParams.put("type", type == null ? "all" : type);
            urlParams.putAll(params);
        } else {
            url.append("/").append(entity).append("/");
            Collection<String> list = params.values();
            for (Iterator<String> it = list.iterator(); it.hasNext();) {
                String value = it.next();
                url.append(value.replace(' ', '+'));
                if (it.hasNext()) {
                    url.append('+');
                }
            }
        }
        url.append("?");
        Iterator<Map.Entry<String, String>> it = urlParams.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, String> e = it.next();
            try {
                String valueEncode = null;
                if (isEncodingEnabled()){
                   valueEncode = URLEncoder.encode(e.getValue(), getEncodingURL());
                }
                else{
                    valueEncode = e.getValue();
                }
                url.append(e.getKey()).append("=").append(valueEncode).append("&");
            } catch (UnsupportedEncodingException ex) {
                logger.error("Internal Error: Could not encode url parameter " + e.getKey(), ex);
            }
        }
        return url.substring(0, url.length() - 1);
    }

    public Resp get(String entity, String type, Map<String, String> params) throws WebServiceException {
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

    private Properties getPropertiesFile() throws WebServiceException{
        Properties properties = new Properties();
        try {
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("discogs-api.properties");
            properties.load(is);
        } catch (IOException e) {
            logger.error(e.getMessage());
            throw new WebServiceException("discogs-api.properties not foud. ", e);
        }
        return properties;
    }

    public String getHostname() throws WebServiceException {
        String hostName = getPropertiesFile().getProperty(HOST_KEY_PROP);
        return hostName != null ? hostName : DEFAULT_HOST;
    }

    public String getProtocol() throws WebServiceException {
        String protocol = getPropertiesFile().getProperty(PROTOCOL_KEY_PROP);
        return protocol != null ? protocol : DEFAULT_PROTOCOL;
    }

    public Integer getPort() throws WebServiceException {
        String protocol = getPropertiesFile().getProperty(PORT_KEY_PROP);
        return protocol != null ? new Integer(protocol) : DEFAULT_PORT;
    }

    public Boolean isEncodingEnabled() throws WebServiceException {
        String enabled = getPropertiesFile().getProperty(ENABLE_URL_ENCODING_KEY_PROP);
        return enabled != null ? new Boolean(enabled) : DEFAULT_URL_ENCODING_ENABLED;
    }

    public String getEncodingURL() throws WebServiceException {
        String urlEncoding = getPropertiesFile().getProperty(URL_ENCODING_KEY_PROP);
        return urlEncoding != null ? urlEncoding : DEFAULT_URL_ENCODING;
    }
}
