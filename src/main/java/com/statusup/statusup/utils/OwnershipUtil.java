package com.statusup.statusup.utils;

import java.util.List;

import org.springframework.stereotype.Component;

import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.CalendarRepository;

@Component
public class OwnershipUtil {

    private JwtUtil jwtUtil;
    private CalendarRepository calendarRepository;

    public OwnershipUtil(JwtUtil jwtUtil, CalendarRepository calendarRepository) {
        this.jwtUtil = jwtUtil;
        this.calendarRepository = calendarRepository;
    }

    public boolean isOwner(Calendar calendar) {
        return jwtUtil.getCurrentUserUsername().equals(calendar.getOwnerUsername());
    }

    public boolean isOwner(String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));
        return isOwner(calendar);
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
