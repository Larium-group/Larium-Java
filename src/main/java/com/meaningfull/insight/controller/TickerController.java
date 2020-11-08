package com.meaningfull.insight.controller;

import com.meaningfull.insight.configuration.CassandraConnector;
import com.meaningfull.insight.configuration.ConfigData;
import com.meaningfull.insight.services.TickerService;
import com.meaningfull.insight.tools.BarsManager;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * Created by Shahaf Pariente on 7/19/2020
 */
@RestController
@RequestMapping({"/tickers"})
public class TickerController {

    @Autowired
    TickerService tickerService;
    @Autowired
    CassandraConnector cassandraConnector;
    @Autowired
    BarsManager barsManager;


    public TickerController() {

    }

    //    @GetMapping({"/getTicker", "/getTicker/"})
//    public Object getTicker(@RequestParam String ticker) {
//        Map<String, Object> tickerMap = new HashMap<>(tickerService.getTickers(ticker));
//        return tickerMap;
//    }
//
//    @GetMapping({"/getTickers", "/getTickers/"})
//    public Object getTickers() {
//        Object quote = tickerService.getQuote();
//        return quote;
//    }
//
//    @GetMapping({"/getHighlight", "/getHighlight/"})
//    public Map<String, Object> getHighlightTickers() {
//        return tickerService.getHighlightTickers();
//    }
//
//    @GetMapping({"/topTickers", "/topTickers/"})
//    public Map<String, Object> getTopOfTheDay() {
//        return tickerService.getTopOfTheDay();
//    }
//
//    @GetMapping({"/portfolio", "/portfolio/"})
//    public Map<String, Object> getPortfolioTickers() {
//        //todo: get user id as param
//        return tickerService.getPortfolioTickers();
//    }

    @GetMapping({"/history", "/history/"})
    public Map<String, Object> getTickerHistory(@RequestParam String ticker, @RequestParam boolean prod) throws ExecutionException, InterruptedException {
        CompletableFuture<Map<String, Object>> historyCompletableFuture = CompletableFuture.supplyAsync(() -> tickerService.getIntraDay(ticker, prod));

        List<Map<String, Object>> rows = cassandraConnector.getRows("SELECT * FROM tweets where stock = '" + ticker + "' ALLOW FILTERING");
        Map<String, List<Map<String, Object>>> barMap = barsManager.getBarMapForSymbol(ticker);
        Map<String, Object> tweetParams = Map.of("tweets", rows);

        Map<String, Object> responseMap = historyCompletableFuture.get();
        ((Map<String, Object>) responseMap.get(ticker)).putAll(barMap);
        ((Map<String, Object>) responseMap.get(ticker)).putAll(tweetParams);
        return responseMap;
    }

    @GetMapping({"/stockdata", "/stockdata/"})
    public Object getStockTable(@RequestParam boolean prod) {
        List<Map<String, Object>> rows = cassandraConnector.getRows("SELECT * FROM stocks");

        ConfigData.tickerList.sort(String::compareTo);
        JSONObject quote = (JSONObject) tickerService.getQuote(prod);
        rows.sort(Comparator.comparing(a -> ((String) a.get("stock"))));

        String timeLabel = (String) rows.get(0).get("fetched_at");
        timeLabel = barsManager.getTheExactTimeLabel(timeLabel);

        for (Map<String, Object> row : rows) {
            String ticker = (String) row.get("stock");
            JSONObject tickerQuote = (JSONObject) ((JSONObject) quote.get(ticker)).get("quote");
            Object price = tickerQuote.get("latestPrice");
            Object volume = tickerQuote.get("volume") != null ? tickerQuote.get("volume") : 0;
            Object change = tickerQuote.get("change");
            Object companyName = tickerQuote.get("companyName");
            Object exchange = tickerQuote.get("primaryExchange");
            barsManager.insertInterestIntoLabel((Double) row.get("last_interest"), ticker, timeLabel);
            row.put("price", price);
            row.put("price_change", change);
            row.put("volume", volume);
            row.put("companyName", companyName);
            row.put("primaryExchange", exchange);
        }
        CompletableFuture.runAsync(() -> barsManager.saveBarsInterestToDb());
        return Map.of("ticker_list", rows);
    }
}
