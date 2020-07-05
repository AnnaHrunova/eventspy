package com.azure.eventmanager.events;

import lombok.Data;

@Data
public class ApplicationInvalidPositionEvent {
    private String applicationReference;
    private String email;
    private String phone;
    private String memberReference;
}
