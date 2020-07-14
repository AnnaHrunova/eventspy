package com.azure.eventmanager.schedulers;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.azure.eventmanager.domain.MlDataEntity;
import com.azure.eventmanager.repository.MlDataRepository;
import com.azure.eventmanager.service.StatisticsService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@AllArgsConstructor
@Component
@Slf4j
public class UpdateRateSummaryJob {

    private final MlDataRepository mlDataRepository;
    private final StatisticsService statisticsService;

    @Scheduled(cron = "0 0 0/1 * * ?")
    public void updateMlData() {
        val currentStatistics = statisticsService.getTotalStatistics();
        val mlData = new MlDataEntity();

        mlData.setDate(LocalDate.now());
        mlData.setApplicationsCount(currentStatistics.getStatistics().getApplicationsCount());
        mlData.setDeclineCount(currentStatistics.getStatistics().getDeclineCount());
        mlData.setSkipCount(currentStatistics.getStatistics().getSkipCount());
        mlData.setInvalidPositionCount(currentStatistics.getStatistics().getInvalidPositionCount());
        mlData.setCheckInCount(currentStatistics.getStatistics().getCheckInCount());
        mlData.setRate(currentStatistics.getStatistics().getRate());

        mlDataRepository.save(mlData);
    }
}
