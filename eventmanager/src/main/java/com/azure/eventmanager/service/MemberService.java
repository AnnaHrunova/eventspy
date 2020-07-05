package com.azure.eventmanager.service;

import java.time.LocalDateTime;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azure.eventmanager.domain.ApplicationEntity;
import com.azure.eventmanager.domain.ContactData;
import com.azure.eventmanager.domain.Coordinates;
import com.azure.eventmanager.domain.EventEntity;
import com.azure.eventmanager.domain.MemberEntity;
import com.azure.eventmanager.domain.Statistics;
import com.azure.eventmanager.domain.Status;
import com.azure.eventmanager.events.ApplicationCheckedInEvent;
import com.azure.eventmanager.events.ApplicationInvalidPositionEvent;
import com.azure.eventmanager.events.ApplyEvent;
import com.azure.eventmanager.repository.ApplicationRepository;
import com.azure.eventmanager.repository.EventRepository;
import com.azure.eventmanager.repository.MemberRepository;
import com.azure.eventmanager.vo.ApplyCommand;
import com.azure.eventmanager.vo.CheckInCommand;
import com.azure.eventmanager.vo.ContactDetails;
import com.azure.eventmanager.vo.DeclineCommand;

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

    public String applyForEvent(ApplyCommand command) {
        EventEntity event = eventRepository.findFirstByCode(command.getEventCode())
                .orElseThrow(() -> new RuntimeException(String.format("Event with code %s not found", command.getEventCode())));

        MemberEntity member = memberRepository.findFirstByMemberReference(command.getMemberReference())
                .map(m -> updateContactDetails(m, command.getContactDetails()))
                .orElse(createNewMember(command));
        member = memberRepository.save(member);
        ApplicationEntity application = ApplicationEntity.builder()
                .event(event)
                .member(member)
                .status(Status.CREATED)
                .contactData(createContactData(command.getContactDetails()))
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

    public void checkIn(CheckInCommand command) {
        ApplicationEntity application = applicationRepository.findFirstByApplicationReference(command.getApplicationReference())
                .orElseThrow(() -> new RuntimeException(String.format("Application %s not found", command.getApplicationReference())));
        checkTimeAndState(application);
        checkPosition(application, command.getPosition());
    }

    public void declineApplication(String applicationReference) {
        ApplicationEntity application = applicationRepository.findFirstByApplicationReference(applicationReference)
                .orElseThrow(() -> new RuntimeException(String.format("Application %s not found", applicationReference)));

        val declineEvent = new ApplicationInvalidPositionEvent();
        declineEvent.setApplicationReference(application.getApplicationReference());
        declineEvent.setEmail(application.getContactData().getEmail());
        declineEvent.setPhone(application.getContactData().getMobilePhone());
        declineEvent.setMemberReference(application.getMember().getMemberReference());
        applicationEventPublisher.publishEvent(declineEvent);

        application.setStatus(Status.DECLINED);
        application.getEvent().declineSpot();
        applicationRepository.save(application);
    }

    public MemberEntity getMemberDetails(String memberReference) {
        return memberRepository.findFirstByMemberReference(memberReference)
                .orElseThrow(() -> new RuntimeException(String.format("Member %s not found", memberReference)));
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

    private void checkPosition(ApplicationEntity application, Coordinates position) {
        val eventCoordinates = application.getEvent().getCoordinates();
        if (eventCoordinates.getLon().equals(position.getLon()) && eventCoordinates.getLat().equals(position.getLat())) {
            val checkInEvent = new ApplicationCheckedInEvent();
            checkInEvent.setApplicationReference(application.getApplicationReference());
            checkInEvent.setEmail(application.getContactData().getEmail());
            checkInEvent.setPhone(application.getContactData().getMobilePhone());
            checkInEvent.setMemberReference(application.getMember().getMemberReference());
            application.setStatus(Status.CHECKED_IN);
            applicationEventPublisher.publishEvent(checkInEvent);
        } else {
            val invalidPositionEvent = new ApplicationInvalidPositionEvent();
            invalidPositionEvent.setApplicationReference(application.getApplicationReference());
            invalidPositionEvent.setEmail(application.getContactData().getEmail());
            invalidPositionEvent.setPhone(application.getContactData().getMobilePhone());
            invalidPositionEvent.setMemberReference(application.getMember().getMemberReference());
            application.setStatus(Status.INVALID_POSITION);
            applicationEventPublisher.publishEvent(invalidPositionEvent);
        }
        applicationRepository.save(application);
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
        memberEntity.setContactData(createContactData(command.getContactDetails()));
        return memberEntity;
    }

    private MemberEntity updateContactDetails(MemberEntity member, ContactDetails contactDetails) {
        ContactData contactData = createContactData(contactDetails);
        member.setContactData(contactData);
        return memberRepository.save(member);
    }

}
