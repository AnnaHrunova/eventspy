package com.azure.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Builder;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Builder
@Data
public class ApplicationEntity {
    private String id;
    private String applicationReference;
    private EventEntity event;
    private MemberEntity member;
    private Status status;
    private ContactData contactData;
    private boolean checkInLinkSent;
}

