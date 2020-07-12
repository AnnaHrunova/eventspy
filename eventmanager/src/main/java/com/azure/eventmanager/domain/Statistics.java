package com.azure.eventmanager.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Statistics {

    private int checkInCount = 0;
    private int declineCount = 0;
    private int skipCount = 0;
    private int invalidPositionCount = 0;
    private int applicationsCount = 0;
    private BigDecimal rate = BigDecimal.ZERO;
    private List<String> platforms = new ArrayList<>();
    private List<String> organizers = new ArrayList<>();

    public void addPlatform(String platform) {
        if (!this.platforms.contains(platform)) {
            platforms.add(platform);
        }
    }

    public void addOrganizer(String organizer) {
        if (!this.organizers.contains(organizer)) {
            organizers.add(organizer);
        }
    }

    public void increaseApplicationsCount() {
        this.applicationsCount += 1;
        this.rate = recalculateRate();
    }

    public void increaseCheckInCount() {
        this.checkInCount += 1;
        this.rate = recalculateRate();
    }

    public void increaseDeclineCount() {
        this.declineCount += 1;
        this.rate = recalculateRate();
    }

    public void increaseSkipCount() {
        this.skipCount += 1;
        this.rate = recalculateRate();
    }

    public void increaseInvalidPositionCount() {
        this.invalidPositionCount += 1;
    }

    private BigDecimal recalculateRate() {
        int totalApplications = checkInCount + declineCount + skipCount + invalidPositionCount;
        if (totalApplications > 0) {
            BigDecimal skipCountCoef = BigDecimal.valueOf(-1.0);
            BigDecimal declineCountCoef = BigDecimal.valueOf(-0.3);
            BigDecimal checkedIdCoef = BigDecimal.ONE;
            return (checkedIdCoef.multiply(new BigDecimal(checkInCount))
                    .add(declineCountCoef.multiply(new BigDecimal(declineCount)))
                    .add(skipCountCoef.multiply(new BigDecimal(skipCount + invalidPositionCount))))
                    .divide(new BigDecimal(totalApplications), RoundingMode.HALF_UP);
        }
        return rate;
    }
}
