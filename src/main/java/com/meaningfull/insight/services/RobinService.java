package com.meaningfull.insight.services;

import com.ampro.robinhood.RobinhoodApi;
import com.ampro.robinhood.endpoint.instrument.data.InstrumentElement;
import com.ampro.robinhood.throwables.RobinhoodApiException;
import com.ampro.robinhood.throwables.TickerNotFoundException;
import com.meaningfull.insight.rest.HttpRequestHandler;
import kong.unirest.HttpResponse;
import org.springframework.stereotype.Service;

/**
 * Created by Shahaf Pariente on 8/3/2020
 */
@Service
public class RobinService {

    final HttpRequestHandler httpRequestHandler;
    String popularityApiUrl = "https://api.robinhood.com/instruments/%1$s/popularity/";

    public RobinService(HttpRequestHandler httpRequestHandler) {
        this.httpRequestHandler = httpRequestHandler;
    }

    public void login() {
        try {
            RobinhoodApi api = new RobinhoodApi();
            api.getInstrumentByTicker("AAPL");
            InstrumentElement aapl = api.getInstrumentByTicker("AAPL");
            String id = aapl.getId();
            String newPopularityUrl = String.format(popularityApiUrl, id);
            HttpResponse<String> body = httpRequestHandler.getMethod(newPopularityUrl);
            System.out.println(body.getBody());
        } catch (RobinhoodApiException | TickerNotFoundException e) {
            e.printStackTrace();
        }
    }
}
