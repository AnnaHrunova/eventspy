package com.azure.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class TotalStatisticsEntity {

    private String id;
    private Statistics statistics = new Statistics();
}
