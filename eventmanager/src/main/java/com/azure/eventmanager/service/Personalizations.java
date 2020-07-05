package com.azure.eventmanager.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Personalizations {
    private String to;
    private String subject;
    private String content;
}
