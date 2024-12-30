package com.statusup.statusup.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.FriendRequest;
import com.statusup.statusup.models.FriendRequestAnswerDTO;
import com.statusup.statusup.models.FriendRequestDTO;
import com.statusup.statusup.services.FriendRequestService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/friends/requests")
public class FriendRequestController {

    private FriendRequestService friendRequestService;

    public FriendRequestController(FriendRequestService friendRequestService) {
        this.friendRequestService = friendRequestService;
    }

    @GetMapping("/received")
    public List<FriendRequest> getAllPendingReceivedFriendRequests() {
        return friendRequestService.getAllPendingReceivedFriendRequests();
    }

    @GetMapping("/sent")
    public List<FriendRequest> getAllPendingSentFriendRequests() {
        return friendRequestService.getAllPendingSentFriendRequests();
    }

    @PostMapping("/send")
    public ResponseEntity<?> sendFriendRequest(@RequestBody FriendRequestDTO friendRequestDTO) {
        return friendRequestService.sendFriendRequest(friendRequestDTO.getUsername());
    }

    @PostMapping("/answer")
    public ResponseEntity<?> answerFriendRequest(@RequestBody FriendRequestAnswerDTO friendRequestAnswerDTO) {
        return friendRequestService.answerRequest(friendRequestAnswerDTO.getFriendRequestId(), friendRequestAnswerDTO.getIsAccepted());
    }
    
}
