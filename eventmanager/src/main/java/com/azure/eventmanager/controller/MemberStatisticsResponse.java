package com.azure.eventmanager.controller;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class MemberStatisticsResponse {
    private String memberId;
    private BigDecimal memberRate;
    private BigDecimal rateByEmail;
    private BigDecimal rateByPhone;

    private Integer checkedInCount;
    private Integer skippedCount;
    private Integer declinedCount;
    private Integer invalidPositionCount;
    private Integer applicationsCount;
}
