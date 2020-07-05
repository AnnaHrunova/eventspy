package com.azure.eventmanager.service;

import java.util.concurrent.ThreadLocalRandom;

import javax.validation.constraints.NotNull;

import org.springframework.stereotype.Component;

import com.azure.eventmanager.domain.Coordinates;
import com.azure.eventmanager.domain.EventEntity;
import com.azure.eventmanager.vo.Event;
import com.azure.eventmanager.vo.Position;
import com.azure.eventmanager.vo.RegisterEventCommand;

@Component
public class EntityMapper {

    public EventEntity map(RegisterEventCommand command, @NotNull Position position) {
        Coordinates coordinates = new Coordinates();
        coordinates.setLat(position.getLat());
        coordinates.setLon(position.getLon());
        return EventEntity.builder()
                .name(command.getName())
                .code("EV-" + ThreadLocalRandom.current().nextInt(100, 998 + 1))
                .organizer(command.getOrganizer())
                .organizerUsername(command.getOrganizerUsername())
                .platform(command.getPlatform())
                .address(command.getAddress())
                .start(command.getEventStart())
                .end(command.getEventEnd())
                .coordinates(coordinates)
                .totalSpots(command.getTotalSpots())
                .address(command.getAddress())
                .rate(command.getRate())
                .reservedSpots(0)
                .build();
    }

    public Event map(EventEntity from) {
        return Event.builder()
                .code(from.getCode())
                .eventStart(from.getStart())
                .eventEnd(from.getEnd())
                .freeSpots(from.getTotalSpots() - from.getReservedSpots())
                .totalSpots(from.getTotalSpots())
                .reservedSpots(from.getReservedSpots())
                .organizer(from.getOrganizer())
                .platform(from.getPlatform())
                .name(from.getName())
                .code(from.getCode())
                .rate(from.getRate())
                .address(from.getAddress())
                .coordinates(formatCoordinates(from))
                .build();
    }

    private String formatCoordinates(final EventEntity from) {
        return from.getCoordinates()
                .getLat() + " " +
                from.getCoordinates()
                .getLon();
    }
}
