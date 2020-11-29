package com.meaningfull.insight.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.meaningfull.insight.rest.HttpRequestHandler;
import com.meaningfull.insight.tools.Scheduler;
import kong.unirest.HttpResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 10/1/2020
 */
@Service
public class PythonScriptRunner implements Scheduler {

    @Autowired
    HttpRequestHandler httpRequestHandler;
    String url = "https://larium-brain.herokuapp.com/";
    Map<String, Object> dataFromFile = null;

    public PythonScriptRunner() {
        setScheduleInitialize(this::getPythonStatus, 5000L);
    }

    public void runScript() {
        httpRequestHandler.getMethod(url + "monitor");
        setScheduleInitialize(this::getPythonStatus, 10000L);
    }

    public void terminateScript() {
        httpRequestHandler.getMethod(url + "terminate");
    }

    public Map<String, Object> getDataFromFile() {
        if (dataFromFile == null)
            getPythonStatus();

        return dataFromFile;
    }

    public void getPythonStatus() {
        HttpResponse<String> response = httpRequestHandler.getMethod(url);
        System.out.println("Checked got python server and status is: " + response.getStatus());
    }

    public List<String> getStocks() {
        Type type = new TypeToken<List<String>>() {
        }.getType();
        HttpResponse<String> response = httpRequestHandler.getMethod(url + "stocks");
        return new Gson().fromJson(response.getBody(), type);
    }

}
