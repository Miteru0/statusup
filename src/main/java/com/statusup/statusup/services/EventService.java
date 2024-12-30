package com.statusup.statusup.services;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.statusup.statusup.exceptions.AccessDeniedException;
import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.models.EventAcquaintanceDTO;
import com.statusup.statusup.models.EventFriendDTO;
import com.statusup.statusup.models.Relationship;
import com.statusup.statusup.repositories.CalendarRepository;
import com.statusup.statusup.repositories.EventRepository;
import com.statusup.statusup.repositories.RelationshipRepository;
import com.statusup.statusup.utils.OwnershipUtil;

@Service
public class EventService {

    private RelationshipRepository relationshipRepository;
    private CalendarRepository calendarRepository;
    private EventRepository eventRepository;
    private OwnershipUtil ownershipUtil;

    public EventService(RelationshipRepository relationshipRepository, CalendarRepository calendarRepository,
            EventRepository eventRepository, OwnershipUtil ownershipUtil) {
        this.relationshipRepository = relationshipRepository;
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
        this.ownershipUtil = ownershipUtil;
    }

    public Object getEvent(String calendarId, String eventId) {

        if (!containsEvent(calendarId, eventId)) {
            throw new ResourceNotFoundException("Event in the calendar not found");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        // Checks if the current User owns the calendar (and if so gives all information
        // about calendar)
        if (ownershipUtil.isOwner(calendarId)) {
            return event;
        }

        // Checking for relationships between current user and owner of calendar
        AccessLevel userAccessLevel = ownershipUtil.getAccessLevelByCalendarId(calendarId);
        AccessLevel eventAccessLevel = event.getAccessLevel();

        if (eventAccessLevel == AccessLevel.FRIEND) {
            if (userAccessLevel == AccessLevel.FRIEND) {
                return new EventFriendDTO(event);
            } 
            else {
                throw new AccessDeniedException("The access level is too low");
            }

        } else if (eventAccessLevel == AccessLevel.ACQUAINTANCE) {
            if (userAccessLevel == AccessLevel.FRIEND || userAccessLevel == AccessLevel.ACQUAINTANCE) {
                return new EventAcquaintanceDTO(event);
            } 
            else {
                throw new AccessDeniedException("The access level is too low");
            }
        }
        throw new AccessDeniedException("The access level is too low");
    }

    public ResponseEntity<?> removeEvent(String username, String calendarId, String eventId) {
        if (!ownershipUtil.isOwner(calendarId)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }
        if (containsEvent(calendarId, eventId)) {
            throw new ResourceNotFoundException("Calendar doesn't contain this event");
        }
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found"));
        calendar.removeEventId(eventId);
        calendarRepository.save(calendar);
        eventRepository.delete(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event has been deleted successfully");
    }

    public ResponseEntity<?> redactEvent(String username, String calendarId, String eventId, Event newEvent) {
        if (!ownershipUtil.isOwner(calendarId)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }
        if (!containsEvent(calendarId, eventId)) {
            throw new ResourceNotFoundException("Calendar doesn't contain this event");
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found"));

        updateEventFields(event, newEvent);

        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.OK).body("Event has been redacted successfully");
    }

    private void updateEventFields(Event existingEvent, Event newEvent) {
        if (newEvent.getName() != null) {
            existingEvent.setName(newEvent.getName());
        }
        if (newEvent.getStartDate() != null) {
            existingEvent.setStartDate(newEvent.getStartDate());
        }
        if (newEvent.getEndDate() != null) {
            existingEvent.setEndDate(newEvent.getEndDate());
        }
        if (newEvent.getDescription() != null) {
            existingEvent.setDescription(newEvent.getDescription());
        }
        if (newEvent.getAccessLevel() != null) {
            existingEvent.setAccessLevel(newEvent.getAccessLevel());
        }
        if (newEvent.getNotificationEnabled() != null) {
            existingEvent.setNotificationEnabled(newEvent.getNotificationEnabled());
        }
    }

    private boolean containsEvent(String calendarId, String eventId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar not found"));
        return calendar.getEventsIds().contains(eventId);
    }

}
