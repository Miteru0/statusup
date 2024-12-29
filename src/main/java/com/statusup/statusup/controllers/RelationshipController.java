package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.services.RelationshipService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;


@RestController
@RequestMapping("/relationship")
public class RelationshipController {

    private RelationshipService relationshipService;

    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping()
    public ResponseEntity<?> addRelationship(@RequestBody Relationship relationship) {
        return relationshipService.addRelationship(relationship);
    }

    @GetMapping("/{username}")
    public List<Relationship> getRelationshipByUsername(@PathVariable String username) {
        return relationshipService.getRelationshipByUsername(username);
    }

    @GetMapping("/{username}/friends")
    public List<String> getFriendsByUsername(@PathVariable String username) {
        return relationshipService.getFriendsUsernamesByUsername(username);
    }
    
    @PutMapping("/{relationshipId}/{accessLevel}")
    public ResponseEntity<?> putMethodName(@PathVariable String relationshipId, @PathVariable AccessLevel accessLevel) {
        return relationshipService.changeRelationship(relationshipId, accessLevel);
    }
    

}
