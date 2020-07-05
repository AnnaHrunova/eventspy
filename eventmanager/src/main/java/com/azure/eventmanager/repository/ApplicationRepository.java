package com.azure.eventmanager.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.azure.eventmanager.domain.ApplicationEntity;
import com.azure.eventmanager.domain.Status;

public interface ApplicationRepository extends MongoRepository<ApplicationEntity, String> {

    Optional<ApplicationEntity> findFirstByEventCodeAndMemberMemberReference(String eventCode, String memberId);
    List<ApplicationEntity> findAllByStatusInAndEventEndBefore(List<Status> statuses, LocalDateTime dateTime);
    List<ApplicationEntity> findAllByStatusInAndEventStartBefore(List<Status> statuses, LocalDateTime dateTime);
    Optional<ApplicationEntity> findFirstByApplicationReference(String applicationReference);

}
