package com.meaningfull.insight.api;

import java.util.Map;

/**
 * Created by Shahaf Pariente on 7/23/2020
 */
public interface HeaderTokenizeAPI {
     Map<String, String> getHeaderWithToken();
     String getCompleteUrl(String path);
}
