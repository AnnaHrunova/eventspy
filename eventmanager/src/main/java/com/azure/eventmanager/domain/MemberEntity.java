package com.azure.eventmanager.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class MemberEntity {
    private String id;
    private String memberReference;
    private ContactData contactData;
    private Statistics statistics = new Statistics();
}

