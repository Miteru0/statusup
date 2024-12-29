package com.statusup.statusup.services;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.statusup.statusup.models.FriendRequest;
import com.statusup.statusup.models.RequestStatus;
import com.statusup.statusup.repositories.FriendRequestRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class FriendRequestService {

    private FriendRequestRepository friendRequestRepository;
    private JwtUtil jwtUtil;

    public FriendRequestService(FriendRequestRepository friendRequestRepository, JwtUtil jwtUtil) {
        this.friendRequestRepository = friendRequestRepository;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> sendFriendRequest(String username) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderUsername(jwtUtil.getCurrentUserUsername());
        friendRequest.setReceiverUsername(username);
        friendRequest.setCreatedAt(LocalDate.now());
        friendRequest.setRequestStatus(RequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sended successfully!");
    }

}
