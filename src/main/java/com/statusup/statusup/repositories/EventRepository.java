package com.statusup.statusup.repositories;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.statusup.statusup.models.Event;

public interface EventRepository extends MongoRepository<Event, String> {
    Optional<Event> findById(String id);
}
