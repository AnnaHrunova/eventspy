package com.azure.eventmanager.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterEventCommand {
    private String name;
    private String organizer;
    private String platform;
    private String address;
    private LocalDateTime eventStart;
    private LocalDateTime eventEnd;
    private int totalSpots;
    private BigDecimal rate;
}
