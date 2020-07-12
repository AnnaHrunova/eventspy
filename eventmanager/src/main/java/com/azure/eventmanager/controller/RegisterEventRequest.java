package com.azure.eventmanager.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

@Data
public class RegisterEventRequest {
    private String name;
    private String organizer;
    private String platform;
    private String address;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime eventStart;

    @DateTimeFormat(pattern = "dd.MM.yyyy HH:mm")
    private LocalDateTime eventEnd;
    private int totalSpots;
    private BigDecimal rate;
}
