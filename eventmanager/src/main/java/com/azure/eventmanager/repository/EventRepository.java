package com.azure.eventmanager.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.azure.eventmanager.domain.EventEntity;

public interface EventRepository extends MongoRepository <EventEntity, String> {

    Optional<EventEntity> findFirstByCode(String code);

    List<EventEntity> findAllByOrganizer(String organizer);
}
