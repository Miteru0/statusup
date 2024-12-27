package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.services.RelationshipService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
public class RelationshipController {

    private RelationshipService relationshipService;

    public RelationshipController(RelationshipService relationshipService) {
        this.relationshipService = relationshipService;
    }

    @PostMapping("/relationship")
    public String addRelationship(@RequestBody Relationship relationship) {
        return relationshipService.addRelationship(relationship);
    }

}
