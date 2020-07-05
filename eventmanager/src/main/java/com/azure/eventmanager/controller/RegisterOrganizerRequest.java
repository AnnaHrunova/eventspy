package com.azure.eventmanager.controller;

import lombok.Data;

@Data
public class RegisterOrganizerRequest {
    private String organizerUsername;
    private String organizerName;
}
