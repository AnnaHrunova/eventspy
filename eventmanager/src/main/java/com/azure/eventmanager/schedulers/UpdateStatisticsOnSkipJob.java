package com.azure.eventmanager.schedulers;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.azure.eventmanager.service.EventManagerService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Component
@Slf4j
public class UpdateStatisticsOnSkipJob {

    private final EventManagerService eventManagerService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void checkSkippedEvents() {
        eventManagerService.resolveSkippedApplications();
    }
}
