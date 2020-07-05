package com.azure.eventmanager.repository;

import com.azure.eventmanager.domain.EventEntity;
import com.azure.eventmanager.domain.OrganizationEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface OrganizationRepository extends MongoRepository <OrganizationEntity, String> {

    Optional<OrganizationEntity> findFirstByOrganizerUsername(String username);
}
