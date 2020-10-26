package com.meaningfull.insight.tools;

import com.meaningfull.insight.configuration.ConfigData;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 10/26/2020
 */
@Service
public class BarsManager {

    Map<String, Map<String, Object>> symbolsBarMap;
    DateTime currentTimeMap = new DateTime();

    public BarsManager() {
        symbolsBarMap = new HashMap<>();
        initBarsMap();
    }

    public void initBarsMap() {
        ConfigData.tickerList.forEach(symbol -> symbolsBarMap.put(symbol, Map.of("bars", new LinkedList<Map<String, Object>>())));
    }

    public List<Map<String, Object>> getBarListForSymbol(String symbol) {
        return (List<Map<String, Object>>) symbolsBarMap.get(symbol).get("bars");
    }

    public Map<String, Object> getBarMapForSymbol(String symbol) {
        return symbolsBarMap.get(symbol);
    }

    public void insertInterestIntoLabel(double interest, String symbol, String label) {
        List<Map<String, Object>> barsMapForSymbol = getBarListForSymbol(symbol);

        Map<String, Object> labelMap = new HashMap<>();
        labelMap.put("label", label);
        labelMap.put("interest", interest);
        barsMapForSymbol.add(labelMap);
    }

    public String getTheExactTimeLabel(String label) {
        DateTimeFormatter fullDateFormat = DateTimeFormat.forPattern("MM/dd/yyyy, HH:mm:ss");
        DateTime time = fullDateFormat.parseDateTime(label);

        if(time.getDayOfMonth() > currentTimeMap.getDayOfMonth() && time.getMonthOfYear() > currentTimeMap.getMonthOfYear() && time.getYear() > currentTimeMap.getYear()) {
            initBarsMap();
            currentTimeMap = time;
        }

        if (time.getMinuteOfHour() > 30) {
            return time.getHourOfDay() + ":30";
        } else {
            return time.getHourOfDay() + ":00";
        }
    }
}
