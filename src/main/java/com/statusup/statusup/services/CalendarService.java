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

    public String createCalendar(Calendar calendar) {

        if (calendar.getOwnerUsername() == null) { calendar.setOwnerUsername(ownershipUtil.getCurrentUserUsername()); }
        
        if (!ownershipUtil.isOwner(calendar)) {
            return "Authentication failure";
        }

        calendar.setEventsIds(new ArrayList<String>());
        calendarRepository.save(calendar);
        return "Successfully created calendar";
        
    }

    public String redactCalendar(String userId, String calendarId, Calendar newCalendar) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar with id " + calendarId + " not found"));
        if (!ownershipUtil.isOwner(calendar)) {
            return "Authentication failure";
        }
        updateCalendarFields(calendar, newCalendar);
        calendarRepository.save(calendar);
        return "Successfully redacted the calendar!";
    }

    private void updateCalendarFields(Calendar existingCalendar, Calendar newCalendar) {
        if (newCalendar.getName() != null) {
            existingCalendar.setName(newCalendar.getName());
        }
    }

    public Object getCalendar(String userId, String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar with id " + calendarId + " not found"));
        if (ownershipUtil.isOwner(calendar)) {
            return calendar;
        }

        List<Relationship> relationships = relationshipRepository.findAllByUserId(userId);
        if (ownershipUtil.getAccessLevel(relationships) == AccessLevel.FRIEND) {
            return calendar;
        } else {
            Calendar empty = new Calendar();
            empty.setName("User didn't give you permission to look at his calendar");
            empty.setOwnerUsername(ownershipUtil.getAccessLevel(relationships).name());
            return empty;
        }
    }

    public String deleteCalendar(String userId, String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar with id " + calendarId + " not found"));

        if (!ownershipUtil.isOwner(calendar)) {
            return "Authentication failure";
        }

        eventRepository.deleteAllById(calendar.getEventsIds());
        calendarRepository.delete(calendar);
        return "Successfully deleted Calendar";
    }

    public String addEvent(String calendarId, Event event) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new NoSuchElementException("Calendar with id " + calendarId + " not found"));
        
        if (ownershipUtil.isOwner(calendar)) {
            return "Authentication failure";
        }

        eventRepository.save(event);
        calendar.addEventId(event.getId());
        calendarRepository.save(calendar);
        return "Successfully added new event to the callendar";
    }

}
