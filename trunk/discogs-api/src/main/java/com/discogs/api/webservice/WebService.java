/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.discogs.api.webservice;

import com.discogs.api.exception.WebServiceException;
import com.discogs.api.model.Resp;

/**
 *
 * @author nexse
 */
public interface WebService {

    public Resp get(String entity, String type, String[] params) throws WebServiceException;
    public void post();

}
