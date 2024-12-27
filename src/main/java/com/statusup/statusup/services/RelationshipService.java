package com.statusup.statusup.services;

import org.springframework.stereotype.Service;

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
        if (isOwner(relationship)) {
            relationshipRepository.save(relationship);
            return "Successfully added a relationship";
        }
        return "The ownerUsername doesn't pass to current username. Authentication failure";
    }

}
