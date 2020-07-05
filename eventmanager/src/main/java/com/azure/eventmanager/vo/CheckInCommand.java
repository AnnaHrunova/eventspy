package com.azure.eventmanager.vo;

import com.azure.eventmanager.domain.Coordinates;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckInCommand {
    private String applicationReference;
    private Coordinates position;
}
