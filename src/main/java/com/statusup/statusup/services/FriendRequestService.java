package com.statusup.statusup.services;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.statusup.statusup.exceptions.AccessDeniedException;
import com.statusup.statusup.exceptions.AlreadyExistsException;
import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.FriendRequest;
import com.statusup.statusup.models.RequestStatus;
import com.statusup.statusup.repositories.FriendRequestRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class FriendRequestService {

    private UserService userService;
    private FriendRequestRepository friendRequestRepository;
    private RelationshipService relationshipService;
    private JwtUtil jwtUtil;

    

    public FriendRequestService(UserService userService, FriendRequestRepository friendRequestRepository,
            RelationshipService relationshipService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.friendRequestRepository = friendRequestRepository;
        this.relationshipService = relationshipService;
        this.jwtUtil = jwtUtil;
    }

    public ResponseEntity<?> sendFriendRequest(String receiverUsername) {
        if (!userService.userExists(receiverUsername)) {
            throw new ResourceNotFoundException("User with username" + receiverUsername + " doesn't exist");
        }
        String currentUserUsername = jwtUtil.getCurrentUserUsername();
        if (isAlreadySent(currentUserUsername, receiverUsername)) {
            throw new AlreadyExistsException("Friend request was already sent!");
        }
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSenderUsername(currentUserUsername);
        friendRequest.setReceiverUsername(receiverUsername);
        friendRequest.setCreatedAt(LocalDate.now());
        friendRequest.setRequestStatus(RequestStatus.PENDING);
        friendRequestRepository.save(friendRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Friend request sent successfully!");
    }

    public ResponseEntity<?> answerRequest(String friendRequestId, boolean isAccepted) {
        FriendRequest friendRequest = friendRequestRepository.findById(friendRequestId)
                .orElseThrow(() -> new ResourceNotFoundException("Friend request not found"));
        if (!jwtUtil.getCurrentUserUsername().equals(friendRequest.getReceiverUsername())) {
            throw new AccessDeniedException("Request is not for you to answer!");
        }
        if (isAccepted) {
            friendRequest.setRequestStatus(RequestStatus.ACCEPTED);
            relationshipService.addFriends(friendRequest.getSenderUsername(), friendRequest.getReceiverUsername());
        } else {
            friendRequest.setRequestStatus(RequestStatus.REJECTED);
        }
        friendRequestRepository.save(friendRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Friend has been answered successfully!");
    }

    public List<FriendRequest> getAllPendingReceivedFriendRequests() {
        List<FriendRequest> friendRequests = friendRequestRepository
                .findAllByReceiverUsername(jwtUtil.getCurrentUserUsername());
        if (friendRequests == null || friendRequests.isEmpty()) {
            return null;
        }
        return friendRequests.stream().filter((list -> list.getRequestStatus() == RequestStatus.PENDING))
                .collect(Collectors.toList());
    }

    public List<FriendRequest> getAllPendingSentFriendRequests() {
        List<FriendRequest> friendRequests = friendRequestRepository
                .findAllBySenderUsername(jwtUtil.getCurrentUserUsername());
        if (friendRequests == null || friendRequests.isEmpty()) {
            return null;
        }
        return friendRequests.stream().filter((list -> list.getRequestStatus() == RequestStatus.PENDING))
                .collect(Collectors.toList());
    }

    private boolean isAlreadySent(String senderUsername, String receiverUsername) {

        List<FriendRequest> sentRequests = friendRequestRepository.findAllBySenderUsername(senderUsername);
        List<FriendRequest> receivedRequests = friendRequestRepository.findAllBySenderUsername(receiverUsername);

        // Check if the current user has already sent a request to the receiver
        boolean isSentByCurrentUser = sentRequests != null && !sentRequests.isEmpty() && sentRequests.stream()
                .map(FriendRequest::getReceiverUsername)
                .anyMatch(username -> username.equals(receiverUsername));

        // Check if the receiver has already sent a request to the current user
        boolean isSentByReceiver = receivedRequests != null && !receivedRequests.isEmpty() && receivedRequests.stream()
                .map(FriendRequest::getReceiverUsername)
                .anyMatch(username -> username.equals(senderUsername));

        return isSentByCurrentUser || isSentByReceiver;
    }

    

}
