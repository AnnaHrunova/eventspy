package com.azure.eventmanager.service;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import lombok.AllArgsConstructor;
import org.apache.http.client.fluent.Request;
import org.apache.http.entity.ContentType;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.apache.http.client.fluent.Content;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
public class MlService {

    private final RestTemplate restTemplate;
    private final String uri;

    // Handle making the request
    public void sendRequest() {
        try {
            String data = "{\"data\": [ { \"instant\": 1, \"date\": \"2021-01-01 00:00:00,000000\", \"season\": 1, \"yr\": 0, \"mnth\": 1, \"weekday\": 6, \"checkedin\": 50, \"skipped\": 10, \"declined\": 5, \"invalid\": 3 } ]}";
            RequestEntity<String> requestEntity = RequestEntity .post(new URL(uri).toURI()) .contentType(MediaType.APPLICATION_JSON) .body(data);
            restTemplate.exchange(requestEntity, String.class);
            // Create the request
            Content content = Request.Post(uri)
                    .addHeader("Content-Type", "application/json")
                    .bodyString(data, ContentType.APPLICATION_JSON)
                    .execute().returnContent();
            System.out.println(content);
        }
        catch (IOException e) {
            System.out.println(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
