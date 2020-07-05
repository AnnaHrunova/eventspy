package com.azure.eventmanager.vo;

import lombok.Data;

@Data
public class EmailCommunication {
    private String to;
    private String subject;
    private String content;
    private String type;
}
