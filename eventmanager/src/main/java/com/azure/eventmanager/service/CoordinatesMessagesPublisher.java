package com.azure.eventmanager.service;

import com.azure.eventmanager.vo.CheckInCommand;
import com.azure.eventmanager.vo.CheckInMessage;
import com.google.gson.reflect.TypeToken;
import com.microsoft.azure.servicebus.*;
import com.microsoft.azure.servicebus.primitives.ConnectionStringBuilder;
import com.google.gson.Gson;
import lombok.val;
import org.springframework.stereotype.Component;

import static java.nio.charset.StandardCharsets.*;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.*;

@Component
public class CoordinatesMessagesPublisher {

    static final Gson GSON = new Gson();

    public void publish(String queueName, CheckInMessage checkInMessage) throws Exception {
        QueueClient sendClient = new QueueClient(new ConnectionStringBuilder("Endpoint=sb://evspyq.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=jg8z5qNnOLO9DeeSi5JZpzE3q3xJJntKMHOuNRC1P4Y=", queueName), ReceiveMode.PEEKLOCK);
        sendMessageAsync(sendClient, checkInMessage).thenRunAsync(() -> sendClient.closeAsync());
    }

    CompletableFuture<Void> sendMessageAsync(QueueClient sendClient, CheckInMessage command) {
        val body = GSON.toJson(command);
        HashMap<String, String> data =
                GSON.fromJson(body, new TypeToken<HashMap<String, String>>() {}.getType());

        List<CompletableFuture> tasks = new ArrayList<>();
        final String messageId = UUID.randomUUID().toString();
        Message message = new Message(GSON.toJson(data, Map.class).getBytes(UTF_8));
        message.setContentType("application/json");
        message.setLabel("Coordinates");
        message.setMessageId(messageId);
        message.setTimeToLive(Duration.ofMinutes(2));
        System.out.printf("\nMessage sending: Id = %s", message.getMessageId());
        tasks.add(
                sendClient.sendAsync(message).thenRunAsync(() -> {
                    System.out.printf("\n\tMessage acknowledged: Id = %s", message.getMessageId());
                }));
        return CompletableFuture.allOf(tasks.toArray(new CompletableFuture<?>[tasks.size()]));
    }
}
