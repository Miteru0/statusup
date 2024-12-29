package com.statusup.statusup.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.models.EventAcquaintanceDTO;
import com.statusup.statusup.models.EventFriendDTO;
import com.statusup.statusup.models.ExceptionDTO;
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

    public Object getEvent(String username, String calendarId, String eventId) {

        if (!containsEvent(calendarId, eventId)) {
            return new ExceptionDTO("Non existent", "The calendar doesn't contatin such event");
        }

        Event event = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("Event not found"));

        // Checks if the current User owns the calendar (and if so gives all information
        // about calendar)
        if (ownershipUtil.isOwner(calendarId)) {
            return event;
        }

        // Checking for relationships between current user and owner of calendar
        List<Relationship> relationships = relationshipRepository.findAllByUsername(username);
        AccessLevel userAccessLevel = ownershipUtil.getAccessLevel(relationships);
        AccessLevel eventAccessLevel = event.getAccessLevel();

        if (eventAccessLevel == AccessLevel.FRIEND) {
            if (userAccessLevel == AccessLevel.FRIEND) {
                return new EventFriendDTO(event.getName(), event.getStartDate(), event.getEndDate(),
                        event.getDescription());
            } else {
                return new ExceptionDTO("Authorization error", "Access level is too low");
            }

        } else if (eventAccessLevel == AccessLevel.ACQUAINTANCE) {
            if (userAccessLevel == AccessLevel.FRIEND || userAccessLevel == AccessLevel.ACQUAINTANCE) {
                return new EventAcquaintanceDTO(event.getStartDate(), event.getEndDate());
            } else {
                return new ExceptionDTO("Authorization error", "Access level is too low");
            }
        }
        return new ExceptionDTO("Authorization error", "Access level is too low");
    }

    public String removeEvent(String username, String calendarId, String eventId) {
        if (!ownershipUtil.isOwner(calendarId)) {
            return "Access denied.";
        }
        if (containsEvent(calendarId, eventId)) {
            return "Calendar doesn't contain such event.";
        }
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));
        calendar.removeEventId(eventId);
        calendarRepository.save(calendar);
        eventRepository.delete(event);
        return "Successfully removed the event!";
    }

    public String redactEvent(String username, String calendarId, String eventId, Event newEvent) {
        if (!ownershipUtil.isOwner(calendarId)) {
            return "Access denied.";
        }

        if (!containsEvent(calendarId, eventId)) {
            return "Calendar doesn't contain such event.";
        }

        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found"));

        updateEventFields(event, newEvent);

        eventRepository.save(event);
        return "Successfully redacted the event!";
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
        if (newEvent.isNotificationEnabled() != null) {
            existingEvent.setNotificationEnabled(newEvent.isNotificationEnabled());
        }
    }

    private boolean containsEvent(String calendarId, String eventId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new RuntimeException("Calendar not found"));
        return calendar.getEventsIds().contains(eventId);
    }

}
