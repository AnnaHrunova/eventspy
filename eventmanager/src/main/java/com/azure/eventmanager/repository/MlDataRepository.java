package com.azure.eventmanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.azure.eventmanager.domain.MlDataEntity;

@Repository
public interface MlDataRepository extends MongoRepository<MlDataEntity, String> {

}
