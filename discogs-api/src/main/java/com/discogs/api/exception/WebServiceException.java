/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.discogs.api.exception;

/**
 *
 * @author nexse
 */
public class WebServiceException extends Exception {

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
