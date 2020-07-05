package com.azure.eventmanager.domain;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public enum Status {
    CREATED,
    CHECKED_IN,
    INVALID_POSITION,
    DECLINED,
    SKIPPED,
    LINK_SENT;

    public static List<Status> getActiveStatuses() {
        return Collections.singletonList(CREATED);
    }
    public static List<Status> getStatusesAllowedForCheckIn() {
        return Arrays.asList(CREATED, INVALID_POSITION);
    }
}
