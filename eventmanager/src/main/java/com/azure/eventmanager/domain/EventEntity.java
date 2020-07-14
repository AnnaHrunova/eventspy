package com.azure.eventmanager.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
public class EventEntity {
    private String id;
    private String code;
    private String organizer;
    private String platform;
    private String name;

    private int reservedSpots;
    private int totalSpots;
    private BigDecimal rate;
    private String address;
    private Coordinates coordinates;
    private LocalDateTime start;
    private LocalDateTime end;

    public void reserveSpot() {
        if (this.totalSpots - this.reservedSpots < 1) {
            throw new RuntimeException(String.format("No free spots available for event: %s: %s", this.getCode(), this.getName()));
        }
        this.reservedSpots += 1;
    }

    public void declineSpot() {
        this.reservedSpots -= 1;
    }
}
