package com.azure.eventmanager.vo;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class Position {
    private BigDecimal lat;
    private BigDecimal lon;
}
