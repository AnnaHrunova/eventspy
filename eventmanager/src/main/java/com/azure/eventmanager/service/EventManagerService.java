package com.azure.eventmanager.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.azure.eventmanager.domain.OrganizationEntity;
import com.azure.eventmanager.repository.OrganizationRepository;
import org.springframework.stereotype.Service;

import com.azure.eventmanager.domain.ApplicationEntity;
import com.azure.eventmanager.domain.EventEntity;
import com.azure.eventmanager.domain.Status;
import com.azure.eventmanager.events.ApplicationSkippedEvent;
import com.azure.eventmanager.maps.AzureMapsService;
import com.azure.eventmanager.repository.ApplicationRepository;
import com.azure.eventmanager.repository.EventRepository;
import com.azure.eventmanager.vo.EmailCommunication;
import com.azure.eventmanager.vo.Event;
import com.azure.eventmanager.vo.Position;
import com.azure.eventmanager.vo.RegisterEventCommand;

import lombok.AllArgsConstructor;
import lombok.val;

@AllArgsConstructor
@Service
public class EventManagerService {

    private final EventRepository eventRepository;
    private final OrganizationRepository organizationRepository;
    private final ApplicationRepository applicationRepository;
    private final AzureMapsService azureMapsService;
    private final EntityMapper entityMapper;
    private final CommunicationService communicationService;

    public String getOrganizerName(String username) {
        return organizationRepository.findFirstByOrganizerUsername(username)
                .map(OrganizationEntity::getOrganizer)
                .orElse(null);
    }

    public void saveOrUpdateOrganizerName(String username, String organizerName) {
        val organizer = organizationRepository.findFirstByOrganizerUsername(username)
                .map(org -> updateOrganizer(organizerName, org))
                .orElse(createNewOrganizer(username, organizerName));
        organizationRepository.save(organizer);
    }

    private OrganizationEntity createNewOrganizer(String username, String organizerName) {
        val org = new OrganizationEntity();
        org.setOrganizer(organizerName);
        org.setOrganizerUsername(username);
        return org;
    }

    private OrganizationEntity updateOrganizer(String organizerName, OrganizationEntity org) {
        org.setOrganizer(organizerName);
        return org;
    }

    public List<Event> findEvents(String organizerUsername) {
        val result = eventRepository.findAllByOrganizerUsername(organizerUsername);
        return result.stream()
                .map(entityMapper::map)
                .collect(Collectors.toList());
    }

    public Event findByCode(final String code) {
        return eventRepository.findFirstByCode(code)
                .map(entityMapper::map)
                .orElseThrow(() -> new RuntimeException(String.format("Event with code %s not found", code)));
    }

    public String registerEvent(RegisterEventCommand command) {
        Position position = azureMapsService.getAddressPosition(command.getAddress());
        EventEntity event = entityMapper.map(command, position);
        eventRepository.save(event);
        return event.getCode();
    }

    public void resolveSkippedApplications() {
        applicationRepository.findAllByStatusInAndEventEndBefore(Status.getActiveStatuses(), LocalDateTime.now())
                .forEach(this::publishApplicationSkippedEvent);
    }

    public void sendCheckInLinks() {
        applicationRepository.findAllByStatusInAndEventStartBefore(Status.getActiveStatuses(), LocalDateTime.now().plusHours(1))
                .forEach(this::sendLink);
    }

    private void publishApplicationSkippedEvent(ApplicationEntity applicationEntity) {
        val event = new ApplicationSkippedEvent();
        event.setEmail(applicationEntity.getContactData().getEmail());
        event.setPhone(applicationEntity.getContactData().getMobilePhone());
        event.setApplicationReference(applicationEntity.getApplicationReference());
        event.setMemberReference(applicationEntity.getMember().getMemberReference());

        applicationEntity.setStatus(Status.SKIPPED);
        applicationRepository.save(applicationEntity);
    }

    private void sendLink(ApplicationEntity applicationEntity) {
        EmailCommunication communication = new EmailCommunication();
        val checkInLonk = String.format("http://localhost:8080/%s/check-in", applicationEntity.getApplicationReference());
        communication.setTo("anna.hrunova93@gmail.com");
        communication.setSubject("Check in!");
        communication.setContent(String.format("Please, follow the link: %s to check-in", ""));
        communicationService.sendEmail(communication);

        applicationEntity.setStatus(Status.LINK_SENT);
        applicationRepository.save(applicationEntity);
    }
}
