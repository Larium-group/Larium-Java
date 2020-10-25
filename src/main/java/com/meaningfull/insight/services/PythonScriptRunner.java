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
    String url = "http://localhost:5000/";
    Map<String, Object> dataFromFile = null;

    public void runScript() {
        httpRequestHandler.getMethod(url + "monitor");
        setScheduleInitialize(this::getFileData, 3000L);
    }

    public void terminateScript() {
        httpRequestHandler.getMethod(url + "terminate");
    }

    public Map<String, Object> getDataFromFile() {
        if(dataFromFile == null)
            getFileData();

        return dataFromFile;
    }

    public void getFileData() {
        Type type = new TypeToken<Map<String, Object>>(){}.getType();
        HttpResponse<String> response = httpRequestHandler.getMethod(url + "getFile");
        dataFromFile = new Gson().fromJson(response.getBody(), type);
    }

    public List<String> getStocks() {
        Type type = new TypeToken<List<String>>(){}.getType();
        HttpResponse<String> response = httpRequestHandler.getMethod(url + "stocks");
        return new Gson().fromJson(response.getBody(), type);
    }

}
