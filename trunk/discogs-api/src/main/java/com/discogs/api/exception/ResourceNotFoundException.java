/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.discogs.api.exception;

/**
 *
 * @author nexse
 */
public class ResourceNotFoundException extends WebServiceException {

    private String response;

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String message, String response) {
        super(message);
        this.response = response;
    }

    public String getResponse() {
        return response;
    }

}
