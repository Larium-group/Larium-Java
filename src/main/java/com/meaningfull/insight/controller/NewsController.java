package com.meaningfull.insight.controller;

import com.meaningfull.insight.services.NewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/19/2020
 */
@RestController
@RequestMapping({"/news"})
public class NewsController {
    @Autowired
    NewsService newsService;

    @GetMapping({"/getNews","/getNews/"})
    public Map<String, Object> getNews(){
        return Map.of("News", newsService.getNews());
    }

    @GetMapping({"/getTickerNews","/getTickerNews/"})
    public Map<String, Object> getNewsForTicker(@RequestParam String ticker, @RequestParam boolean prod){
        return Map.of("News", newsService.getNewsForTicker(ticker, prod));
    }
}
