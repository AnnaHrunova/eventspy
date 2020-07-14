package com.azure.eventmanager.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.eventmanager.vo.EmailCommunication;
import com.microsoft.azure.eventgrid.TopicCredentials;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommunicationService {

    private final CommunicationSender communicationSender;

    private final TopicCredentials topicCredentials;

    @Value("${eventGridEndpoint}")
    private String eventGridEndpoint;

    public void sendApplicationConfirmationEmail(final String email,
                                                 final String eventCode) {
        EmailCommunication communication = new EmailCommunication();
        communication.setTo("anna.hrunova93@gmail.com");
        communication.setSubject("Thank You For Application");
        communication.setContent(String.format("You have just applied for the event: %s.", 123) +
                String.format("To decline your application, please follow the link: %s", 123));
        communication.setType("application.link");
        communicationSender.sendEmail(communication);
    }

    public void sendCheckInLink(final String email,
                                final String applicationReference) {
        EmailCommunication communication = new EmailCommunication();

        val checkInLink = String.format("http://localhost:8080/%s/check-in", applicationReference);
        communication.setTo("anna.hrunova93@gmail.com");
        communication.setSubject("Check in!");
        communication.setContent(String.format("Please, follow the link: %s to check-in", checkInLink));
        communication.setType("checkin.link");
        communicationSender.sendEmail(communication);
    }


}
