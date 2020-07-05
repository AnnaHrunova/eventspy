package com.azure.eventmanager.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class RegisterEventRequest {
    private String name;
    private String organizer;
    private String platform;
    private String address;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private int totalSpots;
    private BigDecimal rate;
}
