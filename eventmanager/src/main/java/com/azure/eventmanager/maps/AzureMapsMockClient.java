package com.azure.eventmanager.maps;

import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class AzureMapsMockClient implements AzureMapsApiService {

    public Pair<HttpStatus, String> getAddressResponse(final String address) {
        String mockedResponse = "{ \"summary\": { \"query\": \"15127 NE 24th Street, Redmond, WA 98052\", \"queryType\": \"NON_NEAR\", \"queryTime\": 58, \"numResults\": 1, \"offset\": 0, \"totalResults\": 1, \"fuzzyLevel\": 1 }, \"results\": [ { \"type\": \"Point Address\", \"id\": \"US/PAD/p0/19173426\", \"score\": 14.51, \"address\": { \"streetNumber\": \"15127\", \"streetName\": \"NE 24th St\", \"municipalitySubdivision\": \"Redmond\", \"municipality\": \"Redmond, Adelaide, Ames Lake, Avondale, Earlmount\", \"countrySecondarySubdivision\": \"King\", \"countryTertiarySubdivision\": \"Seattle East\", \"countrySubdivision\": \"WA\", \"postalCode\": \"98052\", \"extendedPostalCode\": \"980525544\", \"countryCode\": \"US\", \"country\": \"United States Of America\", \"countryCodeISO3\": \"USA\", \"freeformAddress\": \"15127 NE 24th St, Redmond, WA 980525544\", \"countrySubdivisionName\": \"Washington\" }, \"position\": { \"lat\": 47.6308, \"lon\": -122.1385 }, \"viewport\": { \"topLeftPoint\": { \"lat\": 47.6317, \"lon\": -122.13983 }, \"btmRightPoint\": { \"lat\": 47.6299, \"lon\": -122.13717 } }, \"entryPoints\": [ { \"type\": \"main\", \"position\": { \"lat\": 47.6315, \"lon\": -122.13852 } } ] } ] }";
        return Pair.of(HttpStatus.OK, mockedResponse);
    }
}
