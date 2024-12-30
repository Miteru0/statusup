package com.statusup.statusup.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.statusup.statusup.exceptions.AccessDeniedException;
import com.statusup.statusup.exceptions.ResourceNotFoundException;
import com.statusup.statusup.models.AccessLevel;
import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.CalendarDTO;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.models.EventAcquaintanceDTO;
import com.statusup.statusup.models.EventFriendDTO;
import com.statusup.statusup.repositories.CalendarRepository;
import com.statusup.statusup.repositories.EventRepository;
import com.statusup.statusup.utils.OwnershipUtil;

@Service
public class CalendarService {

    private CalendarRepository calendarRepository;
    private EventRepository eventRepository;
    private OwnershipUtil ownershipUtil;

    public CalendarService(CalendarRepository calendarRepository, EventRepository eventRepository,
            OwnershipUtil ownershipUtil) {
        this.calendarRepository = calendarRepository;
        this.eventRepository = eventRepository;
        this.ownershipUtil = ownershipUtil;
    }

    public ResponseEntity<?> createCalendar(Calendar calendar) {

        if (calendar.getOwnerUsername() == null) {
            calendar.setOwnerUsername(ownershipUtil.getCurrentUserUsername());
        }

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

    public Object getCalendar(String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar with id " + calendarId + " not found"));
        if (ownershipUtil.isOwner(calendar)) {
            return calendar;
        }
        AccessLevel accessLevel = ownershipUtil.getAccessLevelByUsername(calendar.getOwnerUsername());
        if (accessLevel == AccessLevel.FRIEND) {
            return new CalendarDTO(calendar.getId(), calendar.getName(), calendar.getOwnerUsername());
        } else {
            throw new AccessDeniedException("The access level is too low");
        }
    }

    public Object getAllCalendarsByUsername(String username) {
        List<Calendar> calendars = Optional.ofNullable(calendarRepository.findAllByOwnerUsername(username))
                .orElse(Collections.emptyList());
        if (ownershipUtil.isCurrentUser(username)) {
            return calendars;
        }
        AccessLevel accessLevel = ownershipUtil.getAccessLevelByUsername(username);

        if (accessLevel == AccessLevel.NONE) {
            throw new AccessDeniedException("The access level is too low");
        }

        if (accessLevel == AccessLevel.FRIEND) {
            List<CalendarDTO> friendCalendarDTOs = calendars.stream()
                .filter(calendar -> calendar.getAccessLevel() == AccessLevel.FRIEND || calendar.getAccessLevel() == AccessLevel.ACQUAINTANCE)
                .map(CalendarDTO::new)
                .collect(Collectors.toList());
            return friendCalendarDTOs;
        }

        if (accessLevel == AccessLevel.ACQUAINTANCE) {
            List<CalendarDTO> acquaintanceCalendarDTOs = calendars.stream()
                .filter(calendar -> calendar.getAccessLevel() == AccessLevel.ACQUAINTANCE)
                .map(CalendarDTO::new)
                .collect(Collectors.toList());
            return acquaintanceCalendarDTOs;
        }
        
        throw new RuntimeException("Something went wrong");
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

        if (!ownershipUtil.isOwner(calendar)) {
            throw new AccessDeniedException("You have to be owner to perform this task");
        }
        if (event.getAccessLevel() == null) {
            event.setAccessLevel(calendar.getAccessLevel());
        }
        event.setCalendarId(calendarId);
        eventRepository.save(event);
        calendar.addEventId(event.getId());
        calendarRepository.save(calendar);
        return ResponseEntity.status(HttpStatus.CREATED).body("Event is added successfully");
    }

    public Object getAllEventsByCalendarId(String calendarId) {
        Calendar calendar = calendarRepository.findById(calendarId)
                .orElseThrow(() -> new ResourceNotFoundException("Calendar with id " + calendarId + " not found"));
        List<Event> events = eventRepository.findAllByCalendarId(calendarId);
        if (ownershipUtil.isOwner(calendarId)) {
            return events;
        }

        AccessLevel userAccessLevel = ownershipUtil.getAccessLevelByUsername(calendar.getOwnerUsername());

        if (userAccessLevel == AccessLevel.NONE) {
            throw new AccessDeniedException("The access level is too low");
        }

        if (userAccessLevel == AccessLevel.FRIEND) {
            List<EventFriendDTO> friendEvents = events.stream()
                    .filter(event -> event.getAccessLevel() == AccessLevel.FRIEND
                            || event.getAccessLevel() == AccessLevel.ACQUAINTANCE)
                    .map(EventFriendDTO::new)
                    .collect(Collectors.toList());
            return friendEvents;
        }

        if (userAccessLevel == AccessLevel.ACQUAINTANCE) {
            List<EventAcquaintanceDTO> acquaintanceEvents = events.stream()
                    .filter(event -> event.getAccessLevel() == AccessLevel.ACQUAINTANCE)
                    .map(EventAcquaintanceDTO::new)
                    .collect(Collectors.toList());
            return acquaintanceEvents;
        }

        throw new RuntimeException("Something went wrong");
    }

}
