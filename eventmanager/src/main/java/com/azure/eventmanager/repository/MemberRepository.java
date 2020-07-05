package com.azure.eventmanager.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.azure.eventmanager.domain.MemberEntity;

@Repository
public interface MemberRepository extends MongoRepository<MemberEntity, String> {

    Optional<MemberEntity> findFirstByMemberReference(String memberId);

}
