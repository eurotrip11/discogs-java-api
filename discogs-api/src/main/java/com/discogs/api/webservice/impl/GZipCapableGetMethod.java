package com.discogs.api.webservice.impl;

import java.io.IOException;
import java.util.zip.GZIPInputStream;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpConnection;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpRecoverableException;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.methods.GetMethod;

/**
 * Override execute and read response methods to allow gzipped content
 * 
 * @author Some guy on the internet
 *
 */
public class GZipCapableGetMethod extends GetMethod {

    public GZipCapableGetMethod(String uri) {
        super(uri);
    }

    /**
     * Overrides method in {@link HttpMethodBase}.
     *
     * Notifies the server that we can process a GZIP-compressed response before
     * sending the request.
     *
     */
    public int execute(HttpState state, HttpConnection conn) throws HttpException, HttpRecoverableException,
            IOException {
        // Tell the server that we can handle GZIP-compressed data in the response body
        addRequestHeader("Accept-Encoding", "gzip");

        return super.execute(state, conn);
    }

    /**
     * Overrides method in {@link GetMethod} to set the responseStream variable appropriately.
     *
     * If the response body was GZIP-compressed, responseStream will be set to a GZIPInputStream
     * wrapping the original InputStream used by the superclass.
     *
     */
    protected void readResponseBody(HttpState state, HttpConnection conn) throws IOException,
            HttpException {
        super.readResponseBody(state, conn);

        Header contentEncodingHeader = getResponseHeader("Content-Encoding");

        if (contentEncodingHeader != null && contentEncodingHeader.getValue().equalsIgnoreCase("gzip")) {
            setResponseStream(new GZIPInputStream(getResponseStream()));
        }
    }
}
