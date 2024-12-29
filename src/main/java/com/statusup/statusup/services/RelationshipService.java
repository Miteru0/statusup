package com.statusup.statusup.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.statusup.statusup.exceptions.AccessDeniedException;
import com.statusup.statusup.exceptions.RelationshipAlreadyExistsException;
import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.RelationshipRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class RelationshipService {

    private RelationshipRepository relationshipRepository;
    private JwtUtil jwtUtil;

    

    public RelationshipService(RelationshipRepository relationshipRepository, JwtUtil jwtUtil) {
        this.relationshipRepository = relationshipRepository;
        this.jwtUtil = jwtUtil;
    }

    private boolean isOwner(Relationship relationship) {
        return jwtUtil.getCurrentUserUsername().equals(relationship.getUsername());
    }

    public ResponseEntity<?> addRelationship(Relationship relationship) {
        if (!isOwner(relationship)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }
        if (doesRelationshipExist(relationship)) {
            throw new RelationshipAlreadyExistsException("Same relationship already exists!");
        }
        relationshipRepository.save(relationship);
        return ResponseEntity.status(HttpStatus.CREATED).body("Relation has been added successfully");

    }

    public ResponseEntity<?> changeRelationship(String relationshipId, AccessLevel newAccessLevel) {
        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find such relationship"));

        if (!isOwner(relationship)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }

        relationship.setAccessLevel(newAccessLevel);
        relationshipRepository.save(relationship);
        return ResponseEntity.status(HttpStatus.OK).body("Relation has been changed successfully");
    }

    public List<Relationship> getRelationshipByUsername(String username) {
        return relationshipRepository.findAllByUsername(username);
    }

    public List<String> getFriendsUsernamesByUsername(String username) {
        return getRelationshipByUsername(username).stream().map(Relationship::getFriendUsername).collect(Collectors.toList());
    }

    private boolean doesRelationshipExist(Relationship relationship) {
        return relationshipRepository.findAllByUsername(relationship.getUsername()).stream()
                .anyMatch(existing -> existing.getFriendUsername().equals(relationship.getFriendUsername()));
    }

}
