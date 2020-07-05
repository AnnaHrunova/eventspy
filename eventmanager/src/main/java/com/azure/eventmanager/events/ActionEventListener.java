package com.azure.eventmanager.events;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.azure.eventmanager.service.StatisticsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
@Component
public class ActionEventListener {

    private final StatisticsService statisticsService;

    @EventListener
    public void onApplicationSkippedEvent(ApplicationSkippedEvent event) {
        statisticsService.updateEmailStatisticsOnSkip(event.getEmail());
        statisticsService.updatePhoneStatisticsOnSkip(event.getPhone());
        statisticsService.updateMemberStatisticsOnSkip(event.getEmail());
    }

    @EventListener
    public void onApplicationCheckedInEvent(ApplicationCheckedInEvent event) {
        statisticsService.updateEmailStatisticsOnCheckIn(event.getEmail());
        statisticsService.updatePhoneStatisticsOnCheckIn(event.getPhone());
        statisticsService.updateMemberStatisticsOnCheckIn(event.getEmail());
    }

    @EventListener
    public void onApplicationDeclinedEvent(ApplicationDeclinedEvent event) {
        statisticsService.updateEmailStatisticsOnDecline(event.getEmail());
        statisticsService.updatePhoneStatisticsOnDecline(event.getPhone());
        statisticsService.updateMemberStatisticsOnDecline(event.getEmail());
    }

    @EventListener
    public void onApplicationInvalidPositionEvent(ApplicationInvalidPositionEvent event) {
        statisticsService.updateEmailStatisticsOnInvalidPosition(event.getEmail());
        statisticsService.updatePhoneStatisticsOnInvalidPosition(event.getPhone());
        statisticsService.updateMemberStatisticsOnInvalidPosition(event.getEmail());
    }

    @EventListener
    public void onApplyEvent(ApplyEvent event) {
        statisticsService.updateEmailStatisticsOnApply(event.getEmail());
        statisticsService.updatePhoneStatisticsOnApply(event.getPhone());
        statisticsService.updateMemberStatisticsOnApply(event.getEmail());
    }
}
