package com.statusup.statusup.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.statusup.statusup.models.Calendar;

public interface CalendarRepository  extends MongoRepository<Calendar, String> {
    Optional<Calendar> findByOwnerUsername(String ownerUsername);
    Optional<Calendar> findById(String id);
    Optional<Calendar> findByName(String name);
    List<Calendar> findAllByOwnerUsername(String ownerUsername);
}
