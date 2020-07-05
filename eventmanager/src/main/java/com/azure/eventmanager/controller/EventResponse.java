package com.azure.eventmanager.controller;

import com.azure.eventmanager.domain.Coordinates;

import lombok.Data;

@Data
public class EventResponse {

    private String code;
    private String organizer;
    private String platform;
    private String name;
    private String description;
    private String address;
    private Coordinates coordinates;
}
