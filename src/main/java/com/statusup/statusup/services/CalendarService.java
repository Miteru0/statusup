package com.statusup.statusup.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import com.statusup.statusup.exceptions.AccessDeniedException;
import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.CalendarDTO;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.CalendarRepository;
import com.statusup.statusup.repositories.EventRepository;
import com.statusup.statusup.repositories.RelationshipRepository;
import com.statusup.statusup.utils.OwnershipUtil;

@Service
public class CalendarService {

    private CalendarRepository calendarRepository;
    private EventRepository eventRepository;
    private RelationshipRepository relationshipRepository;
    private OwnershipUtil ownershipUtil;

    public CalendarService(CalendarRepository calendarRepository, EventRepository eventRepository,
            RelationshipRepository relationshipRepository, OwnershipUtil ownershipUtil) {
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
        this.relationshipRepository = relationshipRepository;
        this.ownershipUtil = ownershipUtil;
    }

    public ResponseEntity<?> createCalendar(Calendar calendar) {

        if (calendar.getOwnerUsername() == null) { calendar.setOwnerUsername(ownershipUtil.getCurrentUserUsername()); }
        
        if (!ownershipUtil.isOwner(calendar)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }

        calendar.setEventsIds(new ArrayList<String>());
        calendarRepository.save(calendar);
        return ResponseEntity.status(HttpStatus.CREATED).body("Calendar created successfully");        
    }

    public ResponseEntity<?> redactCalendar(String username, String calendarId, Calendar newCalendar) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar with id " + calendarId + " not found"));
        if (!ownershipUtil.isOwner(calendar)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }
        updateCalendarFields(calendar, newCalendar);
        calendarRepository.save(calendar);
        return ResponseEntity.status(HttpStatus.OK).body("Calendar redacted successfully!");

    }

    private void updateCalendarFields(Calendar existingCalendar, Calendar newCalendar) {
        if (newCalendar.getName() != null) {
            existingCalendar.setName(newCalendar.getName());
        }
    }

    public Object getCalendar(String username, String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar with id " + calendarId + " not found"));
        if (ownershipUtil.isOwner(calendar)) {
            return calendar;
        }

        List<Relationship> relationships = relationshipRepository.findAllByUsername(username);
        if (ownershipUtil.getAccessLevel(relationships) == AccessLevel.FRIEND) {
            return new CalendarDTO(calendar.getName(), calendar.getOwnerUsername());
        } else {
            throw new AccessDeniedException("The access level is too low");
        }
    }

    public ResponseEntity<?> deleteCalendar(String username, String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar with id " + calendarId + " not found"));

        if (!ownershipUtil.isOwner(calendar)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }

        eventRepository.deleteAllById(calendar.getEventsIds());
        calendarRepository.delete(calendar);
        return ResponseEntity.status(HttpStatus.OK).body("Calendar has been deleted successfully");

    }

    public ResponseEntity<?> addEvent(String calendarId, Event event) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar with id " + calendarId + " not found"));
        
        if (ownershipUtil.isOwner(calendar)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }

        eventRepository.save(event);
        calendar.addEventId(event.getId());
        calendarRepository.save(calendar);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event is added successfully");
    }

}
