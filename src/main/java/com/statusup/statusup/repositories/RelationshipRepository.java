package com.statusup.statusup.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.statusup.statusup.models.Relationship;

public interface RelationshipRepository extends MongoRepository<Relationship, String> {
    Optional<Relationship> findByUserId(String userId);
    List<Relationship> findAllByUserId(String userId);
}
