package com.meaningfull.insight.api;

import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/23/2020
 */

public interface ApiInterface {
    Map<String, Object> getNews();
    Map<String, Object> getNewsForTicker(String symbol, boolean proMode);
    Map<String, Object> getTicker(String symbol, boolean proMode);
    Map<String, Object> getHighlightTickers(boolean proMode);
    Object getTickerHistory(String symbol, String range, boolean proMode);
}
