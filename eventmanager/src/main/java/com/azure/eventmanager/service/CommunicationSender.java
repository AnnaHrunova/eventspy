package com.azure.eventmanager.service;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.azure.eventmanager.vo.EmailCommunication;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import com.microsoft.azure.eventgrid.EventGridClient;
import com.microsoft.azure.eventgrid.TopicCredentials;
import com.microsoft.azure.eventgrid.implementation.EventGridClientImpl;
import com.microsoft.azure.eventgrid.models.EventGridEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CommunicationSender {

    private final TopicCredentials topicCredentials;

    @Value("${eventGridEndpoint}")
    private String eventGridEndpoint;

    public void sendEmail(final EmailCommunication event) {
        try {
            EventGridClient client = new EventGridClientImpl(topicCredentials);

            log.info("Publishing email event to EventGrid");
            Personalizations personalizations = new Personalizations(event.getTo(), event.getSubject(), event.getContent());
            ObjectMapper mapper = new ObjectMapper();
            mapper.registerModule(new JodaModule());
            List<EventGridEvent> eventsList = new ArrayList<>();
            eventsList.add(new EventGridEvent(
                    UUID.randomUUID().toString(),
                    "Subj1",
                    mapper.writeValueAsString(personalizations),
                    event.getType(),
                    DateTime.now(),
                    "1.0"
            ));

            String host = new URI(eventGridEndpoint).getHost();
            String eventGridEndpoint = String.format("https://%s/", host);

            client.publishEvents(eventGridEndpoint, eventsList);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }


}
