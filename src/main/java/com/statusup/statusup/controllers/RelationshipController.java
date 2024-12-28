package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.services.RelationshipService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

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
    public String addRelationship(@RequestBody Relationship relationship) {
        return relationshipService.addRelationship(relationship);
    }

    @GetMapping("/{userId}")
    public List<Relationship> getRelationshipByUserId(@PathVariable String userId) {
        return relationshipService.getRelationshipByUserId(userId);
    }
    
    @PutMapping("/{relationshipId}/{accessLevel}")
    public String putMethodName(@PathVariable String relationshipId, @PathVariable AccessLevel accessLevel) {
        return relationshipService.changeRelationship(relationshipId, accessLevel);
    }
    

}
