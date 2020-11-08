package com.meaningfull.insight.configuration;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang3.StringUtils;
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
            try {
                StringBuilder sb = new StringBuilder(row.getFormattedContents());
                String replaced = sb.replace(0, 1, "{").replace(sb.length() - 1, sb.length(), "}").toString();
                JsonParser parser = new JsonParser();
                JsonObject object = parser.parse(replaced).getAsJsonObject();
                Map<String, Object> params = new Gson().fromJson(object, type);

                rows.add(params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return rows;
    }

    public void runQuery(String query, List<String> queryParams) {
        PreparedStatement preparedQuery = cqlSession.prepare(query);

    }

    public <T> void runBatch(List<T> objectToPrepare) {
        BatchStatementBuilder batch = BatchStatement.builder(BatchType.LOGGED);

        for(T obj : objectToPrepare) {
            SimpleStatement simple = SimpleStatement.builder(obj.toString()).build();
            batch.addStatement(simple);
        }

        cqlSession.execute(batch.build());
    }
}
