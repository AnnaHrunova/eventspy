package com.azure.eventmanager.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ApplyCommand {
    private String eventCode;
    private String memberReference;
    private ContactDetails contactDetails;
}
