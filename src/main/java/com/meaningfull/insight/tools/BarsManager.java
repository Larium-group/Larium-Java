package com.meaningfull.insight.tools;

import com.meaningfull.insight.configuration.CassandraConnector;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by Shahaf Pariente on 10/26/2020
 */
@Service
public class BarsManager {

    Map<String, Map<String, List<Map<String, Object>>>> symbolsBarMap;
    DateTime currentTimeMap = new DateTime();
    @Autowired
    CassandraConnector connector;

    public BarsManager() {
        symbolsBarMap = new HashMap<>();
//        initBarsMap();
        setScheduleInitialize(this::getBarInterestFromDb);
    }

    private void setScheduleInitialize(Runnable func) {
        Timer timer = new Timer(true);
        long delay = 10000L;
        long period = 1000L * 60L * 30L;  // twice a day
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                func.run();
            }
        }, delay, period);
    }

    public void initBarsMap(List<Map<String, Object>> rows) {
        rows.forEach(row -> symbolsBarMap.put(row.get("stock").toString(), Map.of("bars", initLabelsList(row))));
    }

    private void initWithZeroBarsMap() {
        symbolsBarMap.forEach((key, value) -> {
            value.forEach((label, val) -> {
                val.forEach(obj -> obj.put("interest", 0));
            });
        });
    }

    private List<Map<String, Object>> initLabelsList(Map<String, Object> row) {
        List<Map<String, Object>> labelList = new LinkedList<>();
        labelList.add(new HashMap<>() {{
            put("14:30", "14:30");
            put("interest", row.get("14:30"));
        }});
        labelList.add(new HashMap<>() {{
            put("15:00", "15:00");
            put("interest", row.get("15:00"));
        }});
        labelList.add(new HashMap<>() {{
            put("15:30", "15:30");
            put("interest", row.get("15:30"));
        }});
        labelList.add(new HashMap<>() {{
            put("16:00", "16:00");
            put("interest", row.get("16:00"));
        }});
        labelList.add(new HashMap<>() {{
            put("16:30", "16:30");
            put("interest", row.get("16:30"));
        }});
        labelList.add(new HashMap<>() {{
            put("17:00", "17:00");
            put("interest", row.get("17:00"));
        }});
        labelList.add(new HashMap<>() {{
            put("17:30", "17:30");
            put("interest", row.get("17:30"));
        }});
        labelList.add(new HashMap<>() {{
            put("18:00", "18:00");
            put("interest", row.get("18:00"));
        }});
        labelList.add(new HashMap<>() {{
            put("18:30", "18:30");
            put("interest", row.get("18:30"));
        }});
        labelList.add(new HashMap<>() {{
            put("19:00", "19:00");
            put("interest", row.get("19:00"));
        }});
        labelList.add(new HashMap<>() {{
            put("19:30", "19:30");
            put("interest", row.get("19:30"));
        }});
        labelList.add(new HashMap<>() {{
            put("20:00", "20:00");
            put("interest", row.get("20:00"));
        }});
        labelList.add(new HashMap<>() {{
            put("20:30", "20:30");
            put("interest", row.get("20:30"));
        }});
        return labelList;
    }

    public List<Map<String, Object>> getBarListForSymbol(String symbol) {
        return symbolsBarMap.get(symbol).get("bars");
    }

    public Map<String, List<Map<String, Object>>> getBarMapForSymbol(String symbol) {
        return symbolsBarMap.get(symbol);
    }

    public void insertInterestIntoLabel(double interest, String symbol, String label) {
        List<Map<String, Object>> barsMapForSymbol = getBarListForSymbol(symbol);

        for (Map<String, Object> timeLabel : barsMapForSymbol) {
            if (timeLabel.containsKey(label)) {
                timeLabel.put("interest", interest);
            }
        }
    }

    public String getTheExactTimeLabel(String label) {
        DateTimeFormatter fullDateFormat = DateTimeFormat.forPattern("MM/dd/yyyy, HH:mm:ss");
        DateTime time = fullDateFormat.parseDateTime(label);

        // The BarMap resets every day at 14:30 UTC Time
        if (time.getHourOfDay() == 14 && time.getMinuteOfHour() < 30 && time.getMinuteOfHour() > 26 && time.getDayOfWeek() != 1) {
            initWithZeroBarsMap();
            currentTimeMap = time;
        }

        if (time.getMinuteOfHour() > 30) {
            return time.getHourOfDay() + ":30";
        } else {
            return time.getHourOfDay() + ":00";
        }
    }

    public void saveBarsInterestToDb() {
        String query = "INSERT INTO bars (stock, \"14:30\",\"15:00\",\"15:30\",\"16:00\",\"16:30\",\"17:00\",\"17:30\",\"18:00\",\"18:30\",\"19:00\",\"19:30\",\"20:00\",\"20:30\") VALUES ('%1$s',%2$s,%3$s,%4$s,%5$s,%6$s,%7$s,%8$s,%9$s,%10$s,%11$s,%12$s,%13$s,%14$s)";
        List<String> listOfSymbolsInterest = new ArrayList<>();
        symbolsBarMap.forEach((key, value) -> {
            List<String> symbolInterest = new ArrayList<>();
            symbolInterest.add(key);
            value.forEach((label, val) -> val.forEach(obj -> symbolInterest.add(String.valueOf(obj.get("interest")))));
            listOfSymbolsInterest.add(String.format(query, symbolInterest.toArray()));
        });
        connector.runBatch(listOfSymbolsInterest);
    }

    public void getBarInterestFromDb() {
        List<Map<String, Object>> rows = connector.getRows("SELECT * FROM bars");
        initBarsMap(rows);
    }
}
