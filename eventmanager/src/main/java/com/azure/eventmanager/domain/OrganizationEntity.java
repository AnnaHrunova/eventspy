package com.azure.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrganizationEntity {
    private String id;
    private String organizer;
    private String organizerUsername;
}
