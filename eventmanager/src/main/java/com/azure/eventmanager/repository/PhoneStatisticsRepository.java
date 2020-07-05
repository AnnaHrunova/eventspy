package com.azure.eventmanager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.azure.eventmanager.domain.PhoneStatisticsEntity;

@Repository
public interface PhoneStatisticsRepository extends MongoRepository<PhoneStatisticsEntity, String> {

    Optional<PhoneStatisticsEntity> findFirstByPhoneNumber(String phoneNumber);

}
