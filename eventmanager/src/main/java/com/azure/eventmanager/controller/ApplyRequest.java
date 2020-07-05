package com.azure.eventmanager.controller;

import lombok.Data;

@Data
public class ApplyRequest {
    private String eventCode;
    private String memberReference;
    private String email;
    private String mobilePhone;
}
