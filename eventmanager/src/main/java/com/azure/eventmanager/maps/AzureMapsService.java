package com.azure.eventmanager.maps;

import static java.util.Optional.ofNullable;

import java.io.IOException;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import com.azure.eventmanager.vo.AddressPositionResponse;
import com.azure.eventmanager.vo.Position;
import com.azure.eventmanager.vo.Result;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class AzureMapsService {

    private final AzureMapsApiService azureMapsApiService;
    private final ObjectMapper mapper;

    public Position getAddressPosition(final String address) {
        Pair<HttpStatus, String> response = azureMapsApiService.getAddressResponse(address);
        try {
            AddressPositionResponse addressPositionResponse = mapper.readValue(response.getRight(), AddressPositionResponse.class);
            return ofNullable(addressPositionResponse)
                    .map(AddressPositionResponse::getResults)
                    .map(result -> result.get(0))
                    .map(Result::getPosition)
                    .orElse(null);
        } catch (IOException e) {
            log.error(e.getMessage());
            return null;
        }
    }

}
