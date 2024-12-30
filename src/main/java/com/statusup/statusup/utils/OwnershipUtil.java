package com.statusup.statusup.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.CalendarRepository;
import com.statusup.statusup.repositories.RelationshipRepository;

@Component
public class OwnershipUtil {

    private JwtUtil jwtUtil;
    private CalendarRepository calendarRepository;
    private RelationshipRepository relationshipRepository;

    public OwnershipUtil(JwtUtil jwtUtil, CalendarRepository calendarRepository,
            RelationshipRepository relationshipRepository) {
        this.jwtUtil = jwtUtil;
        this.calendarRepository = calendarRepository;
        this.relationshipRepository = relationshipRepository;
    }

    public boolean isOwner(Calendar calendar) {
        return jwtUtil.getCurrentUserUsername().equals(calendar.getOwnerUsername());
    }

    public boolean isOwner(String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));
        return isOwner(calendar);
    }

    public boolean isCurrentUser(String username) {
        return jwtUtil.getCurrentUserUsername().equals(username);
    }

    public AccessLevel getAccessLevelByUsername(String username) {
        List<Relationship> relationships = relationshipRepository.findAllByUsername(username);
        return getAccessLevel(relationships);
    }

    public AccessLevel getAccessLevelByCalendarId(String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Couldn't find calendar with id " + calendarId));
        return getAccessLevelByUsername(calendar.getOwnerUsername());
    }

    public AccessLevel getAccessLevel(List<Relationship> relationships) {
        String currentUserUsername = jwtUtil.getCurrentUserUsername();

        return relationships.stream()
                .filter(relationship -> relationship.getFriendUsername().equals(currentUserUsername))
                .map(Relationship::getAccessLevel)
                .findFirst()
                .orElse(AccessLevel.NONE);
    }

    public String getCurrentUserUsername() {
        return jwtUtil.getCurrentUserUsername();
    }

}
