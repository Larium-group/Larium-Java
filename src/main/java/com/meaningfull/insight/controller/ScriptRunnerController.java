package com.meaningfull.insight.controller;

import com.meaningfull.insight.services.PythonScriptRunner;
import com.meaningfull.insight.services.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.zankowski.iextrading4j.api.stocks.Quote;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 10/1/2020
 */
@RestController
@RequestMapping({"/script"})
public class ScriptRunnerController {

    @Autowired
    PythonScriptRunner pythonScriptRunner;
    @Autowired
    TickerService tickerService;

    @GetMapping({"/runner"})
    public void runScript() {
        pythonScriptRunner.runScript();
    }

    @GetMapping({"/terminate"})
    public void terminateScript() {
        pythonScriptRunner.terminateScript();
    }

    @GetMapping({"/getFile"})
    public Map<String, Object> getFileData() {
        System.out.println("Get File");
        List<String> stocksList = pythonScriptRunner.getStocks();
        List<Quote> quote = tickerService.getQuotes(stocksList);
        List<Map<String, Object>> tickerList = (List<Map<String, Object>>) pythonScriptRunner.getDataFromFile().get("ticker_list");
        tickerList.sort(Comparator.comparing(a -> ((String) a.get("stock"))));
        int len = tickerList.size();
        for(int i=0; i<len; i++) {
            BigDecimal price = quote.get(i).getLatestPrice();
            //TODO : change the CHANGE to PRICE CHANGE
            tickerList.get(i).put("price", price);
        }
        return Map.of("ticker_list", tickerList);
    }
}
