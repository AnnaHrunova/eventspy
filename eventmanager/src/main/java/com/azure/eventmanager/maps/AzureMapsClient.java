package com.azure.eventmanager.maps;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class AzureMapsClient implements AzureMapsApiService {

    private final RestTemplate restTemplate;
    private final String azureMapsUrl;
    private final String azureMapsSubscriptionKey;

    public Pair<HttpStatus, String> getAddressResponse(final String address) {
        try {
            final String requestUrl = String.format(azureMapsUrl, azureMapsSubscriptionKey, address);
            final ResponseEntity<String> response = restTemplate.getForEntity(requestUrl, String.class);
            final HttpStatus statusCode = response.getStatusCode();
            return Pair.of(statusCode, response.getBody());
        } catch (final RuntimeException exception) {
            log.error("Failed to retrieve address response.", exception);
            return Pair.of(null, null);
        }
    }
}
