package com.azure.eventmanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.azure.eventmanager.domain.TotalStatisticsEntity;

@Repository
public interface TotalStatisticsRepository extends MongoRepository<TotalStatisticsEntity, String> {

}
