package com.meaningfull.insight.services;

import com.meaningfull.insight.api.ApiAbstract;
import com.meaningfull.insight.api.IEXApi;
import com.meaningfull.insight.configuration.ConfigData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/19/2020
 */
@Service
@SuppressWarnings("unchecked")
public class TickerService {

    @Autowired
    Map<String, ApiAbstract> apis;

    public Map<String, Object> getTickers(String ticker, boolean proMode) {
        ApiAbstract api = apis.get("IEXApi");
        return api.getTicker(ticker, proMode);
    }

    public Map<String, Object> getCompanyInformation(String ticker, boolean proMode) {
        ApiAbstract api = apis.get("IEXApi");
        return Map.of("company", api.getCompanyInformation(ticker, proMode));
    }

    public Map<String, Object> getHighlightTickers(boolean proMode) {
        return apis.get("IEXApi").getHighlightTickers(proMode);
    }

    public Map<String, Object> getTopOfTheDay() {
//        Map<String, Object> topTickerList = getTickersFromList(ConfigData.tickerList);
//        List<Map.Entry<String, Object>> entries =
//                new ArrayList<>(topTickerList.entrySet());
//        entries.sort((a, b) -> {
//            double aValue = (double) ((Map<String, Object>) a.getValue()).get("previousClose");
//            double bValue = (double) ((Map<String, Object>) b.getValue()).get("previousClose");
//            return Double.compare(bValue, aValue);
//        });
//
//        return Map.of("Top", entries.subList(0, 3), "Lowest", entries.subList(entries.size()-3, entries.size()));
        return null;
    }

    public Map<String, Object> getPortfolioTickers() {
        //todo: get user id as param
        return null;
//        return getTickersFromList(ConfigData.userPortfolioTickers);
    }

    public Map<String, Object> getTickerHistory(String Symbol, boolean proMode) {
        ApiAbstract api = apis.get("IEXApi");
        Map<String, Object> historyRangeList = new LinkedHashMap<>();
        ConfigData.historyRanges.forEach(range -> historyRangeList.put(range, api.getTickerHistory(Symbol, range, proMode)));
        return Map.of(Symbol, historyRangeList);
    }

    public Map<String, Object> getTickersFromList(List<String> tickers, boolean proMode) {
        Map<String, Object> tickersMap = new LinkedHashMap<>();
        tickers.forEach(ticker -> tickersMap.putAll(getTickers(ticker, proMode)));
        return tickersMap;
    }

    public Map<String, Object> getTickersFromList() {
        return null;
//        return getTickersFromList(TickersList.tickersList);
    }

    public Map<String, Object> getIntraDay(String symbol, boolean proMode) {
        ApiAbstract iexApi = apis.get("IEXApi");
        Map<String, Object> intraDayData =  new HashMap<>() {{
            put("1d", ((IEXApi) iexApi).getIntraDayPrice(symbol, proMode));
        }};
        Map<String, Object> responseMap = new HashMap<>() {{
            put(symbol, intraDayData);
        }};
        return responseMap;
    }

    public Object getQuote(boolean proMode) {
        ApiAbstract iexApi = apis.get("IEXApi");
        return ((IEXApi) iexApi).getQuote(proMode);
    }

    public List<Quote> getQuotes(List<String> symbols) {
//        IEXApi iexApi = new IEXApi();
//        List<Quote> quotes = new LinkedList<>();
//        symbols.forEach(symbol -> quotes.add(iexApi.getQuote(symbol)));
//        return quotes;
        return null;
    }

}
