package com.azure.eventmanager.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.eventmanager.controller.MemberStatisticsResponse;
import com.azure.eventmanager.domain.ApplicationEntity;
import com.azure.eventmanager.domain.ContactData;
import com.azure.eventmanager.domain.EventEntity;
import com.azure.eventmanager.domain.MemberEntity;
import com.azure.eventmanager.domain.Statistics;
import com.azure.eventmanager.domain.Status;
import com.azure.eventmanager.events.ApplicationDeclinedEvent;
import com.azure.eventmanager.events.ApplyEvent;
import com.azure.eventmanager.repository.ApplicationRepository;
import com.azure.eventmanager.repository.EventRepository;
import com.azure.eventmanager.repository.MemberRepository;
import com.azure.eventmanager.vo.ApplyCommand;
import com.azure.eventmanager.vo.CheckInCommand;
import com.azure.eventmanager.vo.CheckInMessage;
import com.azure.eventmanager.vo.ContactDetails;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
@Service
@Transactional
public class MemberService {

    private final EventRepository eventRepository;
    private final MemberRepository memberRepository;
    private final ApplicationRepository applicationRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final CoordinatesMessagesPublisher coordinatesPublisher;

    public String applyForEvent(ApplyCommand command) {
        EventEntity event = eventRepository.findFirstByCode(command.getEventCode())
                .orElseThrow(() -> new RuntimeException(String.format("Event with code %s not found", command.getEventCode())));

        MemberEntity member = memberRepository.findFirstByMemberReference(command.getMemberReference())
                .orElse(createNewMember(command));
        member = memberRepository.save(member);
        ApplicationEntity application = ApplicationEntity.builder()
                .event(event)
                .member(member)
                .status(Status.CREATED)
                .contactData(createContactData(command.getContactDetails()))
                .applicationReference(UUID.randomUUID().toString())
                .build();
        application = applicationRepository.save(application);
        event.reserveSpot();
        eventRepository.save(event);

        val applyEvent = new ApplyEvent();
        applyEvent.setApplicationReference(application.getApplicationReference());
        applyEvent.setEmail(command.getContactDetails().getEmail());
        applyEvent.setPhone(command.getContactDetails().getMobilePhone());
        applyEvent.setMemberReference(member.getMemberReference());
        applicationEventPublisher.publishEvent(applyEvent);
        return application.getApplicationReference();
    }

    public void checkIn(CheckInCommand command) throws Exception {
        ApplicationEntity application = applicationRepository.findFirstByApplicationReference(command.getApplicationReference())
                .orElseThrow(() -> new RuntimeException(String.format("Application %s not found", command.getApplicationReference())));
//        checkTimeAndState(application);

        val message = new CheckInMessage();
        message.setApplicationReference(command.getApplicationReference());
        message.setLon(String.valueOf(command.getPosition().getLon()));
        message.setLat(String.valueOf(command.getPosition().getLat()));
        coordinatesPublisher.publish("coordinates", message);
    }

    public void declineApplication(String applicationReference) {
        ApplicationEntity application = applicationRepository.findFirstByApplicationReference(applicationReference)
                .orElseThrow(() -> new RuntimeException(String.format("Application %s not found", applicationReference)));

        val declineEvent = new ApplicationDeclinedEvent();
        declineEvent.setApplicationReference(application.getApplicationReference());
        declineEvent.setEmail(application.getContactData().getEmail());
        declineEvent.setPhone(application.getContactData().getMobilePhone());
        declineEvent.setMemberReference(application.getMember().getMemberReference());
        applicationEventPublisher.publishEvent(declineEvent);

        application.setStatus(Status.DECLINED);
        application.getEvent().declineSpot();
        applicationRepository.save(application);
    }

    public MemberStatisticsResponse getMemberDetails(String memberReference) {
        val member = memberRepository.findFirstByMemberReference(memberReference)
                .orElseThrow(() -> new RuntimeException(String.format("Member %s not found", memberReference)));
        val response = new MemberStatisticsResponse();
        response.setMemberId(memberReference);
        response.setMemberRate(member.getStatistics().getRate());
        response.setCheckedInCount(member.getStatistics().getCheckInCount());
        response.setDeclinedCount(member.getStatistics().getDeclineCount());
        response.setSkippedCount(member.getStatistics().getSkipCount());
        response.setInvalidPositionCount(member.getStatistics().getInvalidPositionCount());
        response.setApplicationsCount(member.getStatistics().getApplicationsCount());
        return response;
    }

    private void checkTimeAndState(ApplicationEntity application) {
        val currentTime = LocalDateTime.now();
        if (!Status.getStatusesAllowedForCheckIn().contains(application.getStatus())) {
            throw new RuntimeException(String.format("CheckIn is not allowed for application: %s because of incorrect status", application.getApplicationReference()));
        }
        if (currentTime.isBefore(application.getEvent().getStart()) || currentTime.isAfter(application.getEvent().getEnd())) {
            throw new RuntimeException(String.format("CheckIn is not allowed for application: %s because of incorrect time frame", application.getApplicationReference()));
        }
    }

    private ContactData createContactData(final ContactDetails contactDetails) {
        ContactData contactData = new ContactData();
        contactData.setEmail(contactDetails.getEmail());
        contactData.setMobilePhone(contactDetails.getMobilePhone());
        return contactData;
    }

    private MemberEntity createNewMember(ApplyCommand command) {
        val memberEntity = new MemberEntity();
        memberEntity.setMemberReference(command.getMemberReference());
        memberEntity.setStatistics(new Statistics());
        return memberEntity;
    }
}
