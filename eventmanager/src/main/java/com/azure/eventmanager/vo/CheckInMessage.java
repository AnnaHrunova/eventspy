package com.azure.eventmanager.vo;

import lombok.Data;

@Data
public class CheckInMessage {

    private String applicationReference;
    private String lon;
    private String lat;
}
