package com.azure.eventmanager.service;

import org.springframework.stereotype.Service;

import com.azure.eventmanager.domain.EmailStatisticsEntity;
import com.azure.eventmanager.domain.MemberEntity;
import com.azure.eventmanager.domain.PhoneStatisticsEntity;
import com.azure.eventmanager.domain.Statistics;
import com.azure.eventmanager.domain.TotalStatisticsEntity;
import com.azure.eventmanager.repository.EmailStatisticsRepository;
import com.azure.eventmanager.repository.MemberRepository;
import com.azure.eventmanager.repository.PhoneStatisticsRepository;
import com.azure.eventmanager.repository.TotalStatisticsRepository;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
@Service
public class StatisticsService {

    private final EmailStatisticsRepository emailStatisticsRepository;
    private final PhoneStatisticsRepository phoneStatisticsRepository;
    private final TotalStatisticsRepository totalStatisticsRepository;
    private final MemberRepository memberRepository;

    public EmailStatisticsEntity getEmailStatistics(String email) {
        return emailStatisticsRepository.findFirstByEmail(email)
                .orElse(createNewEmailStatistics(email));
    }

    public PhoneStatisticsEntity getPhoneStatistics(String phone) {
        return phoneStatisticsRepository.findFirstByPhoneNumber(phone)
                .orElse(createNewPhoneStatistics(phone));
    }

    public TotalStatisticsEntity getTotalStatistics() {
        val stats = totalStatisticsRepository.findAll();
        if (stats.isEmpty()) {
            return new TotalStatisticsEntity();
        }
        return stats.get(0);
    }

    public EmailStatisticsEntity updateEmailStatisticsOnApply(String email) {
        val emailStatistics = getEmailStatistics(email);
        emailStatistics.getStatistics().increaseApplicationsCount();
        return emailStatisticsRepository.save(emailStatistics);
    }

    public PhoneStatisticsEntity updatePhoneStatisticsOnApply(String phone) {

        val phoneStatistics = getPhoneStatistics(phone);
        phoneStatistics.getStatistics().increaseApplicationsCount();
        return phoneStatisticsRepository.save(phoneStatistics);
    }

    public EmailStatisticsEntity updateEmailStatisticsOnCheckIn(String email) {
        val emailStatistics = getEmailStatistics(email);
        emailStatistics.getStatistics().increaseCheckInCount();
        return emailStatisticsRepository.save(emailStatistics);
    }

    public PhoneStatisticsEntity updatePhoneStatisticsOnCheckIn(String phone) {

        val phoneStatistics = getPhoneStatistics(phone);
        phoneStatistics.getStatistics().increaseCheckInCount();
        return phoneStatisticsRepository.save(phoneStatistics);
    }

    public EmailStatisticsEntity updateEmailStatisticsOnDecline(String email) {
        val emailStatistics = getEmailStatistics(email);
        emailStatistics.getStatistics().increaseDeclineCount();
        return emailStatisticsRepository.save(emailStatistics);
    }

    public PhoneStatisticsEntity updatePhoneStatisticsOnDecline(String phone) {

        val phoneStatistics = getPhoneStatistics(phone);
        phoneStatistics.getStatistics().increaseDeclineCount();
        return phoneStatisticsRepository.save(phoneStatistics);
    }

    public EmailStatisticsEntity updateEmailStatisticsOnSkip(String email) {
        val emailStatistics = getEmailStatistics(email);
        emailStatistics.getStatistics().increaseSkipCount();
        return emailStatisticsRepository.save(emailStatistics);
    }

    public PhoneStatisticsEntity updatePhoneStatisticsOnSkip(String phone) {

        val phoneStatistics = getPhoneStatistics(phone);
        phoneStatistics.getStatistics().increaseSkipCount();
        return phoneStatisticsRepository.save(phoneStatistics);
    }

    public EmailStatisticsEntity updateEmailStatisticsOnInvalidPosition(String email) {
        val emailStatistics = getEmailStatistics(email);
        emailStatistics.getStatistics().increaseInvalidPositionCount();
        return emailStatisticsRepository.save(emailStatistics);
    }

    public PhoneStatisticsEntity updatePhoneStatisticsOnInvalidPosition(String phone) {

        val phoneStatistics = getPhoneStatistics(phone);
        phoneStatistics.getStatistics().increaseInvalidPositionCount();
        return phoneStatisticsRepository.save(phoneStatistics);
    }

    private EmailStatisticsEntity createNewEmailStatistics(String email) {
        return EmailStatisticsEntity.builder()
                .email(email)
                .statistics(new Statistics())
                .build();
    }

    private PhoneStatisticsEntity createNewPhoneStatistics(String phone) {
        return PhoneStatisticsEntity.builder()
                .phoneNumber(phone)
                .statistics(new Statistics())
                .build();
    }

    public void updateMemberStatisticsOnApply(String memberReference) {
        MemberEntity member = getMember(memberReference);
        member.getStatistics().increaseApplicationsCount();
        memberRepository.save(member);
    }

    public void updateMemberStatisticsOnCheckIn(String memberReference) {
        MemberEntity member = getMember(memberReference);
        member.getStatistics().increaseCheckInCount();
        memberRepository.save(member);
    }

    public void updateMemberStatisticsOnDecline(String memberReference) {
        MemberEntity member = getMember(memberReference);
        member.getStatistics().increaseDeclineCount();
        memberRepository.save(member);
    }

    public void updateMemberStatisticsOnInvalidPosition(String memberReference) {
        MemberEntity member = getMember(memberReference);
        member.getStatistics().increaseInvalidPositionCount();
        memberRepository.save(member);
    }

    public void updateMemberStatisticsOnSkip(String memberReference) {
        MemberEntity member = getMember(memberReference);
        member.getStatistics().increaseSkipCount();
        memberRepository.save(member);
    }

    public void updateTotalStatisticsOnCheckIn() {
        TotalStatisticsEntity totalStat = getTotalStatistics();
        totalStat.getStatistics().increaseCheckInCount();
        totalStatisticsRepository.save(totalStat);
    }

    public void updateTotalStatisticsOnDecline() {
        TotalStatisticsEntity totalStat = getTotalStatistics();
        totalStat.getStatistics().increaseDeclineCount();
        totalStatisticsRepository.save(totalStat);
    }

    public void updateTotalStatisticsOnApply() {
        TotalStatisticsEntity totalStat = getTotalStatistics();
        totalStat.getStatistics().increaseApplicationsCount();
        totalStatisticsRepository.save(totalStat);
    }

    public void updateTotalStatisticsOnSkip() {
        TotalStatisticsEntity totalStat = getTotalStatistics();
        totalStat.getStatistics().increaseSkipCount();
        totalStatisticsRepository.save(totalStat);
    }

    public void updateTotalStatisticsOnInvalidPosition() {
        TotalStatisticsEntity totalStat = getTotalStatistics();
        totalStat.getStatistics().increaseInvalidPositionCount();
        totalStatisticsRepository.save(totalStat);
    }

    private MemberEntity getMember(final String memberReference) {
        return memberRepository.findFirstByMemberReference(memberReference)
                .orElseThrow(() -> new RuntimeException(String.format("Memeber not found: %s", memberReference)));
    }
}
