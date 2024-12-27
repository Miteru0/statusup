package com.statusup.statusup.repositories;

import java.util.Optional;
import com.statusup.statusup.models.User;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Optional<User> findById(String id);
}
