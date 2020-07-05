package com.azure.eventmanager.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AddressPositionResponse {
    private ArrayList<Result> results;
}
