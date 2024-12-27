package com.statusup.statusup.services;

import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.CalendarRepository;
import com.statusup.statusup.repositories.EventRepository;
import com.statusup.statusup.repositories.RelationshipRepository;
import com.statusup.statusup.utils.JwtUtil;

@Service
public class CalendarService {

    private CalendarRepository calendarRepository;
    private EventRepository eventRepository;
    private RelationshipRepository relationshipRepository;
    private JwtUtil jwtUtil;

    public CalendarService(CalendarRepository calendarRepository, EventRepository eventRepository,
            RelationshipRepository relationshipRepository, JwtUtil jwtUtil) {
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
        this.relationshipRepository = relationshipRepository;
        this.jwtUtil = jwtUtil;
    }

    private boolean isOwner(Calendar calendar) {
        return jwtUtil.getCurrentUserUsername().equals(calendar.getOwnerUsername());
    }

    private AccessLevel accessLevel(List<Relationship> relationships) {
        String currentUserId = jwtUtil.getCurrentUserId();

        
        return relationships.stream()
            .filter(relationship -> relationship.getFriendId().equals(currentUserId))
            .map(Relationship::getAccessLevel)
            .findFirst()
            .orElse(AccessLevel.NONE);
    }

    public String createCalendar(Calendar calendar) {
        if (isOwner(calendar)) {
            if (calendar.getEventsIds() == null) {
                calendar.setEventsIds(new ArrayList<String>());
            }
            calendarRepository.save(calendar);
            return "Successfully created calendar for user " + jwtUtil.getCurrentUserUsername();
        }
        return "The ownerUsername doesn't pass to current username. Authentication failure";
    }

    public String addEvent(String calendarId, Event event) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar with id " + calendarId + " not found"));
        if (isOwner(calendar)) {
            eventRepository.save(event);
            calendar.addEventId(event.getId());
            calendarRepository.save(calendar);
            return "Successfully added new event to the callendar";
        }
        return "The ownerUsername doesn't pass to current username. Authentication failure";
    }

    public Calendar getCalendar(String userId, String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar with id " + calendarId + " not found"));
        if (isOwner(calendar)) {
            return calendar;
        }

        List<Relationship> relationships = relationshipRepository.findAllByUserId(userId);
        if(accessLevel(relationships) == AccessLevel.FRIEND) {
            return calendar;
        }
        else {
            Calendar empty = new Calendar();
            empty.setName("User didn't give you permission to look at his calendar");
            empty.setOwnerUsername(accessLevel(relationships).name());
            return empty;
        }
    }

}
