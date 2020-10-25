package com.meaningfull.insight.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Shahaf Pariente on 10/20/2020
 */
@Component
public class CassandraConnector {

    public CqlSession cqlSession;
    String keySpace = "tickers";

    public CassandraConnector() {
        try {
            cqlSession = CqlSession.builder()
                    // make sure you change the path to the secure connect bundle below
                    .withCloudSecureConnectBundle(Paths.get("secure-connect-ticker-db.zip"))
                    .withAuthCredentials("ticker", "ticker")
                    .withKeyspace(keySpace)
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public void changeKeySpace(String keySpace) {
        try {
            cqlSession = CqlSession.builder()
                    // make sure you change the path to the secure connect bundle below
                    .withCloudSecureConnectBundle(Paths.get("secure-connect-ticker-db.zip"))
                    .withAuthCredentials("ticker", "ticker")
                    .withKeyspace(keySpace)
                    .build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public List<Map<String, Object>> getRows(String query) {
        Type type = new TypeToken<Map<String, Object>>() {
        }.getType();
        ResultSet rs = cqlSession.execute(
                SimpleStatement.builder(query)
                        .build());
        List<Map<String, Object>> rows = new LinkedList<>();
        rs.all().forEach(row -> {
            String replaced = row.getFormattedContents().replace("[", "{").replace("]","}");
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(replaced).getAsJsonObject();
            Map<String, Object> params = new Gson().fromJson(object, type);
            rows.add(params);
        });
        return rows;
    }
}
