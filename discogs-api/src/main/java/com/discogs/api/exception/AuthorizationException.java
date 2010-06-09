/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.discogs.api.exception;

/**
 *
 * @author nexse
 */
public class AuthorizationException extends WebServiceException {

    public AuthorizationException(String message) {
        super(message);
    }

}
