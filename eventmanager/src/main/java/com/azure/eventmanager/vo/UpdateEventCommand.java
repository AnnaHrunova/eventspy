package com.azure.eventmanager.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateEventCommand {
    private String code;
    private String address;
    private int freeSpots;
    private int reservedSpots;
    private BigDecimal rate;
    private LocalDateTime eventDate;
}
