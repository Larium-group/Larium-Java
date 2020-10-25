package com.meaningfull.insight.rest;

import kong.unirest.GetRequest;
import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/19/2020
 */
@Component
public class HttpRequestHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final RestTemplate restTemplate = new RestTemplate();

    public ResponseEntity<?> postRequestHandler(String url, HttpMethod method, HttpEntity<?> entity, Class type) {
        try {
            ResponseEntity<?> response = this.restTemplate.exchange(url, method, entity, type);

            return checkResponseStatus(response);
        } catch (Exception e) {
            logger.error("Could not establish connection " + e);
            return ResponseEntity.notFound().build();
        }
    }

    private ResponseEntity<?> checkResponseStatus(ResponseEntity<?> response) {
        if (!(response.getStatusCodeValue() == 201 || response.getStatusCodeValue() == 200)) {
            logger.error("Error with the API! status: " + response.getStatusCode());
            return ResponseEntity.badRequest().build();
        }

        return response;
    }

    public HttpResponse<String> getMethod(String url, Map<String, String> headers) {
        GetRequest rest = Unirest.get(url);
        headers.forEach(rest::header);
        return getRequest(rest);
    }

    public HttpResponse<String> getMethod(String url) {
        GetRequest rest = Unirest.get(url);
        return getRequest(rest);
    }

    private HttpResponse<String> getRequest(GetRequest request) {
        return request.asString();
    }

}
