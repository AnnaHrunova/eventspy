package com.azure.eventmanager.maps;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;

public interface AzureMapsApiService {

    Pair<HttpStatus, String> getAddressResponse(final String address);

}
