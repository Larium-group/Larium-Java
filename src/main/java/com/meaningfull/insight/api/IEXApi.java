package com.meaningfull.insight.api;

import com.meaningfull.insight.configuration.ConfigData;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/19/2020
 */
@Component
public class IEXApi extends ApiAbstract implements ApiInterface, TokenizeAPI {

    private final String tokenSandBox;
    private final String tokenProd;
    final ConfigData configData;


    public IEXApi(ConfigData configData) {
        this.urlSandBox = "https://sandbox.iexapis.com/stable";
        this.tokenSandBox = "Tpk_c4dc6e0225fd44ed982b13f9a7ae480c";
        this.urlProd = "https://cloud.iexapis.com/stable/";
        this.tokenProd = "pk_931eb2d4b0fd4b6b8c798f05c96888a9";
        this.configData = configData;
    }

    /**************
     Getters
     *************/
    public String getTokenSandBox() {
        return tokenSandBox;
    }


    /**************
     Functions
     *************/

    @Override
    public String getUrlWithToken(String path, boolean prodMode) {
        if(prodMode) return this.urlProd + path + "?token=" + this.tokenProd;
        return this.urlSandBox + path + "?token=" + this.tokenSandBox;
    }


    @Override
    public Map<String, Object> getNews() {
//        logger.info("Getting News from IEX...");
//        Map<String, Object> newsList = new HashMap<>();
//        for (String ticker : ConfigData.tickerList) {
//            newsList.putAll(getNewsForTicker(ticker));
//        }
//        return newsList;
        return null;
    }

    @Override
    public Map<String, Object> getNewsForTicker(String symbol, boolean proMode) {
        logger.info("Getting News for" + symbol + "from IEX...");
        Map<String, Object> newsList = new HashMap<>();
        String newUrl = getUrlWithToken(String.format("/stock/%s/news", symbol),proMode);
        newsList.put(symbol, getResponseBody(httpRequestHandler.getMethod(newUrl)));
        return newsList;
    }

    @Override
    public Map<String, Object> getTicker(String symbol, boolean proMode) {
        logger.info("Getting Tickers from IEX...");
        Map<String, Object> tickerMap = new HashMap<>();
        String newUrl = getUrlWithToken(String.format("/stock/%s/quote", symbol), proMode);
        tickerMap.put(symbol, getResponseBody(httpRequestHandler.getMethod(newUrl)));
        return tickerMap;
    }

    @Override
    public Map<String, Object> getHighlightTickers(boolean proMode) {
        Map<String, Object> highlightTickerMap = new HashMap<>();
//        ConfigData.highlightTickers.forEach(ticker -> highlightTickerMap.putAll(getTicker(ticker)));
        return highlightTickerMap;
    }

    @Override
    public Object getTickerHistory(String symbol, String range, boolean proMode) {
        logger.info("Getting Ticker history from IEX...");
        String newUrl = getUrlWithToken(String.format("/stock/%s/chart/%s", symbol, range), proMode);
        return getResponseBody(httpRequestHandler.getMethod(newUrl));
    }

    @Override
    public Object getCompanyInformation(String symbol, boolean proMode) {
        logger.info("Get " + symbol + " Company Information...");
        String newUrl = getUrlWithToken(String.format("/stock/%s/company", symbol), proMode);
        return getResponseBody(httpRequestHandler.getMethod(newUrl));
    }

    public Object getIntraDayPrice(String symbol, boolean proMode) {
        logger.info("Getting Ticker history from IEX...");
        String newUrl = getUrlWithToken(String.format("/stock/%s/intraday-prices", symbol), proMode);
        return getResponseBody(httpRequestHandler.getMethod(newUrl));
    }

    public Object getQuote(boolean proMode) {
        logger.info("Getting Tickers from IEX...");
        Map<String, Object> tickerMap = new HashMap<>();

        StringBuilder sb = new StringBuilder();
        for (String ticker : ConfigData.tickerList) {
            sb.append(ticker).append(",");
        }
        String url = getUrlWithToken("/stock/market/batch", proMode);
        String newUrl = String.format(url + "&types=quote&symbols=%1$s", StringUtils.chop(sb.toString()));
        return getResponseBody(httpRequestHandler.getMethod(newUrl));
    }
}
