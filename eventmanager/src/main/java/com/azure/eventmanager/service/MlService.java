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

    // Handle making the request
    public void sendRequest(String data) {
        // Replace with the scoring_uri of your service
        String uri = "http://d7427ef8-5ab8-4068-8ec9-2039ef2f9752.northeurope.azurecontainer.io/score";
        // If using authentication, replace with the auth key or token
//        String key = "<your key or token>";
        try {
            data = "{\"data\":[{\"instant\":1,\"date\":\"2021-01-01 00:00:00,000000\",\"season\":1,\"yr\":0,\"mnth\":1,\"weekday\":6,\"weathersit\":2,\"temp\":0.344167,\"atemp\":0.363625,\"hum\":0.805833,\"windspeed\":0.160446}]}";
            RequestEntity<String> requestEntity = RequestEntity .post(new URL(uri).toURI()) .contentType(MediaType.APPLICATION_JSON) .body(data);
            restTemplate.exchange(requestEntity, String.class);
            // Create the request
            Content content = Request.Post(uri)
                    .addHeader("Content-Type", "application/json")
                    // Only needed if using authentication
//                    .addHeader("Authorization", "Bearer " + key)
                    // Set the JSON data as the body
                    .bodyString(data, ContentType.APPLICATION_JSON)
                    // Make the request and display the response.
                    .execute().returnContent();
            System.out.println(content);
        }
        catch (IOException e) {
            System.out.println(e);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public void sendRequest() {
        // Create the data to send to the service
        JSONObject obj = new JSONObject();
        // In this case, it's an array of arrays
        JSONArray dataItems = new JSONArray();
        // Inner array has 10 elements
        JSONArray item1 = new JSONArray();
        item1.add(0.0199132141783263);
        item1.add(0.0506801187398187);
        item1.add(0.104808689473925);
        item1.add(0.0700725447072635);
        item1.add(-0.0359677812752396);
        item1.add(-0.0266789028311707);
        item1.add(-0.0249926566315915);
        item1.add(-0.00259226199818282);
        item1.add(0.00371173823343597);
        item1.add(0.0403433716478807);
        // Add the first set of data to be scored
        dataItems.add(item1);
        // Create and add the second set
        JSONArray item2 = new JSONArray();
        item2.add(-0.0127796318808497);
        item2.add(-0.044641636506989);
        item2.add(0.0606183944448076);
        item2.add(0.0528581912385822);
        item2.add(0.0479653430750293);
        item2.add(0.0293746718291555);
        item2.add(-0.0176293810234174);
        item2.add(0.0343088588777263);
        item2.add(0.0702112981933102);
        item2.add(0.00720651632920303);
//        dataItems.add(item2);
        obj.put("data", dataItems);

        // Make the request using the JSON document string
        sendRequest(obj.toJSONString());
    }
}
