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

package com.discogs.api.webservice.impl;

import com.discogs.api.exception.AuthorizationException;
import com.discogs.api.exception.RequestException;
import com.discogs.api.exception.ResourceNotFoundException;
import com.discogs.api.exception.WebServiceException;
import com.discogs.api.model.Resp;
import com.discogs.api.webservice.DefaultWebService;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientWebService extends DefaultWebService {

    private HttpClient httpClient;
    private Logger logger = LoggerFactory.getLogger(HttpClient.class);

    public HttpClientWebService() {
        httpClient = new HttpClient();
        initConfiguration();
    }

    public HttpClientWebService(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    private void initConfiguration(){
        HttpClientParams hcp = new HttpClientParams();
        hcp.setParameter(HttpClientParams.USER_AGENT, "Discogs Java Api v1.0");
        httpClient.setParams(hcp);
    }

    @Override
    public Resp doGet(String url) throws WebServiceException {
        HttpMethod method = new GZipCapableGetMethod(url);
        method.getParams().setParameter(HttpMethodParams.RETRY_HANDLER, new DefaultHttpMethodRetryHandler(3, false));
        method.setDoAuthentication(true);

        try {
            // execute the method
            int statusCode = this.httpClient.executeMethod(method);

            if (logger.isDebugEnabled()) {
                logger.debug(method.getResponseBodyAsString());
            }

            switch (statusCode) {
                case HttpStatus.SC_OK:
                    return createResp(method.getResponseBodyAsStream());

                case HttpStatus.SC_NOT_FOUND:
                    throw new ResourceNotFoundException("Resource not found.", method.getResponseBodyAsString());

                case HttpStatus.SC_BAD_REQUEST:
                    throw new RequestException(method.getResponseBodyAsString());

                case HttpStatus.SC_FORBIDDEN:
                    throw new AuthorizationException(method.getResponseBodyAsString());

                case HttpStatus.SC_UNAUTHORIZED:
                    throw new AuthorizationException(method.getResponseBodyAsString());

                default:
                    String em = "web service returned unknown status '" + statusCode + "', response was: " + method.getResponseBodyAsString();
                    logger.error(em);
                    throw new WebServiceException(em);
            }
        } catch (HttpException e) {
            logger.error("Fatal protocol violation: " + e.getMessage());
            throw new WebServiceException(e.getMessage(), e);
        } catch (IOException e) {
            logger.error("Fatal transport error: " + e.getMessage());
            throw new WebServiceException(e.getMessage(), e);
        } finally {
            method.releaseConnection();
        }
    }

    @Override
    public void doPost(String url) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Resp createResp(InputStream inputStream) {
        Resp resp = null;
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance("com.discogs.api.model");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            resp = (Resp) unmarshaller.unmarshal(inputStream);
        } catch (JAXBException e) {
            logger.error(e.getMessage());
        }
        return resp;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public void setHttpClient(HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
