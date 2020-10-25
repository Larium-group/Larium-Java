package com.meaningfull.insight.api;

import com.meaningfull.insight.rest.HttpRequestHandler;
import kong.unirest.HttpResponse;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created by Shahaf Pariente on 7/23/2020
 */
@Component
public abstract class ApiAbstract implements ApiInterface {

    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    protected String urlSandBox;
    protected String urlProd;


    @Autowired
    protected HttpRequestHandler httpRequestHandler;

    /**************
     Getters
     *************/



    public String getUrlSandBox() {
        return urlSandBox;
    }

    /**************
     Functions
     *************/

    public Object getResponseBody(HttpResponse<String> response) {
        Object json = null;
        try {
            JSONParser parser = new JSONParser();
            json = parser.parse(response.getBody());
        } catch (ParseException e) {
            logger.error("Error with parsing Api Response...");
        }
        return json;
    }

    public abstract Object getCompanyInformation(String symbol, boolean proMode);


}
