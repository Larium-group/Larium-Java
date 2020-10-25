package com.meaningfull.insight.api;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/23/2020
 */
@Component
public class RapidApi extends ApiAbstract implements ApiInterface, HeaderTokenizeAPI {

    public RapidApi() {
        urlSandBox = "https://apidojo-yahoo-finance-v1.p.rapidapi.com";
    }

    @Override
    public Map<String, String> getHeaderWithToken() {
        return Map.of("x-rapidapi-host", "apidojo-yahoo-finance-v1.p.rapidapi.com", "x-rapidapi-key", "11ad77e335msh9cf3d4e03d74ae4p16f040jsnac0d090933e6");
    }

    @Override
    public String getCompleteUrl(String path) {
        return urlSandBox + path;
    }

    @Override
    public Map<String, Object> getNews() {
//        Map<String, Object> newsList = new HashMap<>();
//        for (String ticker : ConfigData.tickerList) {
//            newsList.putAll(getNewsForTicker(ticker));
//        }
//        return newsList;
        return null;
    }

    @Override
    public Map<String, Object> getNewsForTicker(String symbol, boolean proMode) {
        Map<String, Object> newsList = new HashMap<>();
        String newUrl = getCompleteUrl(String.format("/stock/v2/get-newsfeed?category=%s&region=US", symbol));
        newsList.put(symbol, getResponseBody(httpRequestHandler.getMethod(newUrl, getHeaderWithToken())));
        return newsList;
    }

    @Override
    public Map<String, Object> getTicker(String symbol, boolean proMode) {
        return null;
    }

    @Override
    public Map<String, Object> getHighlightTickers(boolean proMode) {
        return null;
    }

    @Override
    public Object getTickerHistory(String symbol, String range, boolean proMode) {
        return null;
    }

    @Override
    public Object getCompanyInformation(String symbol, boolean proMode) {
        return null;
    }
}
