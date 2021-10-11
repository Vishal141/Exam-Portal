package com.exam.portal.server;

import java.net.HttpURLConnection;
import java.net.URL;

public class ServerConfig {
    private static final String BASE_URL = "http://localhost:8080/exam-portal";

    public static HttpURLConnection getConnection(String path){
        try {
            URL url = new URL(BASE_URL+path);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoOutput(true);
            connection.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            return connection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
