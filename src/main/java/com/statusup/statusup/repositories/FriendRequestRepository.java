package com.statusup.statusup.repositories;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.statusup.statusup.models.FriendRequest;

public interface FriendRequestRepository extends MongoRepository<FriendRequest, String> {
    List<FriendRequest> findAllBySenderUsername(String senderUsername);
    List<FriendRequest> findAllByReceiverUsername(String receiverUsername);
    boolean existsBySenderUsername(String senderUsername);
    boolean existsByReceiverUsername(String receiverUsername);
}
