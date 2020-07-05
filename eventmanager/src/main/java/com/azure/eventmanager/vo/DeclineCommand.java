package com.azure.eventmanager.vo;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DeclineCommand {
    private String applicationReference;
    private String memberReference;
}
