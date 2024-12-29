package com.statusup.statusup.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.FriendRequest;
import com.statusup.statusup.services.FriendRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@RestController
@RequestMapping("/friends/requests")
public class FriendRequestController {

    private FriendRequestService friendRequestService;

    @GetMapping("/received")
    public List<FriendRequest> getAllPendingReceivedFriendRequests() {
        return friendRequestService.getAllPendingReceivedFriendRequests();
    }

    @GetMapping("/sent")
    public List<FriendRequest> getAllPendingSentFriendRequests() {
        return friendRequestService.getAllPendingSentFriendRequests();
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendFriendRequest(@RequestParam String receiverUsername) {
        return friendRequestService.sendFriendRequest(receiverUsername);
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answerFriendRequest(@RequestParam String friendRequestId, @RequestParam Boolean isAccepted) {
        return friendRequestService.answerRequest(friendRequestId, isAccepted);
    }
    
}
