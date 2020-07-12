package com.azure.eventmanager.service;

import com.azure.eventmanager.domain.ApplicationEntity;
import com.azure.eventmanager.domain.Coordinates;
import com.azure.eventmanager.domain.Status;
import com.azure.eventmanager.events.ApplicationCheckedInEvent;
import com.azure.eventmanager.events.ApplicationInvalidPositionEvent;
import com.google.gson.Gson;
import com.microsoft.azure.servicebus.ExceptionPhase;
import com.microsoft.azure.servicebus.IMessage;
import com.microsoft.azure.servicebus.IMessageHandler;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static java.nio.charset.StandardCharsets.UTF_8;

@Slf4j
@AllArgsConstructor
public class CoordinatesMessageListener implements IMessageHandler {

    private final EventManagerService eventManagerService;
    static final Gson GSON = new Gson();

    public CompletableFuture<Void> onMessageAsync(IMessage message) {
        log.info("message received");
        if (message.getLabel() != null &&
                message.getContentType() != null &&
                message.getContentType().contentEquals("application/json")) {

            byte[] body = message.getBody();
            Map checkInData = GSON.fromJson(new String(body, UTF_8), Map.class);

            String applicationReference = checkInData != null ? (String) checkInData.get("applicationReference") : "";
            String lon = checkInData != null ? (String) checkInData.get("lon") : "";
            String lat = checkInData != null ? (String) checkInData.get("lat") : "";

            val coordinates = new Coordinates();
            coordinates.setLat(new BigDecimal(lat));
            coordinates.setLon(new BigDecimal(lon));
            eventManagerService.checkPosition(applicationReference, coordinates);
        }
        return CompletableFuture.completedFuture(null);
    }

    // callback invoked when the message handler has an exception to report
    public void notifyException(Throwable throwable, ExceptionPhase exceptionPhase) {
        log.error(exceptionPhase + "-" + throwable.getMessage());
    }
}
