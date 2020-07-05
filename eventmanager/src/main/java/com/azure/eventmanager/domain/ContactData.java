package com.azure.eventmanager.domain;

import lombok.Builder;
import lombok.Data;

@Data
public class ContactData {
    private String email;
    private String mobilePhone;
}
