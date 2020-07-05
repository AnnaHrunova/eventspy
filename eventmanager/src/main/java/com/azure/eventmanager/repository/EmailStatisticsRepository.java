package com.azure.eventmanager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.azure.eventmanager.domain.EmailStatisticsEntity;

@Repository
public interface EmailStatisticsRepository extends MongoRepository<EmailStatisticsEntity, String> {

    Optional<EmailStatisticsEntity> findFirstByEmail(String email);

}
