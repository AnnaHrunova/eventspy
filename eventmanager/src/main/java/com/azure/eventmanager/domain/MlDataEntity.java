package com.azure.eventmanager.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MlDataEntity {

    private String id;
    private LocalDate date;
    private int checkInCount = 0;
    private int declineCount = 0;
    private int skipCount = 0;
    private int invalidPositionCount = 0;
    private int applicationsCount = 0;
    private BigDecimal rate = BigDecimal.ZERO;
}
