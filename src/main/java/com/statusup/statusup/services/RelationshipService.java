package com.statusup.statusup.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

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
        return jwtUtil.getCurrentUserUsername().equals(relationship.getUserUsername());
    }

    public String addRelationship(Relationship relationship) {
        if (!isOwner(relationship)) {
            return "Authentication failure";
        }

        if (doesRelationshipExist(relationship)) {
            return "Relationship already exist!";
        }
        relationshipRepository.save(relationship);
        return "Successfully added a relationship";

    }

    public String changeRelationship(String relationshipId, AccessLevel newAccessLevel) {
        Relationship relationship = relationshipRepository.findById(relationshipId)
                .orElseThrow(() -> new RuntimeException("Couldn't find such relationship"));

        if (!isOwner(relationship)) {
            return "Authentication failure";
        }

        relationship.setAccessLevel(newAccessLevel);
        relationshipRepository.save(relationship);
        return "Successfully changed a relationship";
    }

    public List<Relationship> getRelationshipByUsername(String username) {
        return relationshipRepository.findAllByUsername(username);
    }

    public List<String> getFriendsUsernamesByUsername(String username) {
        return getRelationshipByUsername(username).stream().map(Relationship::getFriendUsername).collect(Collectors.toList());
    }

    private boolean doesRelationshipExist(Relationship relationship) {
        return relationshipRepository.findAllByUsername(relationship.getUserUsername()).stream()
                .anyMatch(existing -> existing.getFriendUsername().equals(relationship.getFriendUsername()));
    }

}
