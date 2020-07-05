package com.azure.eventmanager.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Event {

    private String name;
    private String code;
    private String organizer;
    private String platform;
    private String organizerUsername;
    private String address;
    private String coordinates;
    private int freeSpots;
    private int reservedSpots;
    private int totalSpots;
    private BigDecimal rate;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
}
