package com.azure.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
public class PhoneStatisticsEntity {
    private String id;
    private String phoneNumber;
    private Statistics statistics = new Statistics();
}
