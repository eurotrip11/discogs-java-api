/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.discogs.api.exception;

/**
 *
 * @author nexse
 */
public class JDiscogsException extends Exception {

    public JDiscogsException(String message, Throwable cause) {
        super(message, cause);
    }

    public JDiscogsException(String message) {
        super(message);
    }

}
