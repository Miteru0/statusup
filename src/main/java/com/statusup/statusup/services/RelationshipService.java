package com.statusup.statusup.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.RelationshipRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class RelationshipService {

    private RelationshipRepository relationshipRepository;
    private JwtUtil jwtUtil;

    public RelationshipService(RelationshipRepository relationshipRepository,
            JwtUtil jwtUtil) {
        this.relationshipRepository = relationshipRepository;
        this.jwtUtil = jwtUtil;
    }

    private boolean isOwner(Relationship relationship) {
        return jwtUtil.getCurrentUserId().equals(relationship.getUserId());
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

    public List<Relationship> getRelationshipByUserId(String userId) {
        return relationshipRepository.findAllByUserId(userId);
    }

    private boolean doesRelationshipExist(Relationship relationship) {
        return relationshipRepository.findAllByUserId(relationship.getUserId()).stream()
                .anyMatch(existing -> existing.getFriendId().equals(relationship.getFriendId()));
    }
    

}
