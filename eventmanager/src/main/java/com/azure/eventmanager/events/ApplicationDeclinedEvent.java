package com.azure.eventmanager.events;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ApplicationDeclinedEvent {
    private String applicationReference;
    private String email;
    private String phone;
    private String memberReference;
}
