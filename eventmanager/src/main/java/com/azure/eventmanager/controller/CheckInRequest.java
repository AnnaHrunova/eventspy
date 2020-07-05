package com.azure.eventmanager.controller;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class CheckInRequest {
    private BigDecimal lat;
    private BigDecimal lon;
}
