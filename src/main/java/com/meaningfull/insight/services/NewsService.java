package com.meaningfull.insight.services;

import com.meaningfull.insight.api.ApiInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/19/2020
 */
@Service
public class NewsService {

    @Autowired
    Map<String, ApiInterface> apis;

    public Map<String, Object> getNews(){
        return apis.get("IEXApi").getNews();
    }

    public Map<String, Object> getNewsForTicker(String symbol, boolean proMode) {
        ApiInterface iex = apis.get("IEXApi");
        return iex.getNewsForTicker(symbol, proMode);
    }
}
