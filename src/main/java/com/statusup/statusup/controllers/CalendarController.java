package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.services.CalendarService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/calendar")
public class CalendarController {

    private CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping()
    public ResponseEntity<?> createCalendar(@RequestBody Calendar calendar) {
        return calendarService.createCalendar(calendar);
    }

    @GetMapping("/{username}/{calendarId}")
    public Object getCalendar(@PathVariable String username, @PathVariable String calendarId) {
        return calendarService.getCalendar(calendarId);
    }

    @DeleteMapping("/{username}/{calendarId}")
    public Object deleteCalendar(@PathVariable String username, @PathVariable String calendarId) {
        return calendarService.deleteCalendar(username, calendarId);
    }

    @PutMapping("{username}/{calendarId}")
    public Object redactCalendar(@PathVariable String username, @PathVariable String calendarId, @RequestBody Calendar calendar) {
        return calendarService.redactCalendar(username, calendarId, calendar);
    }

    @PostMapping("/{calendarId}")
    public ResponseEntity<?> addEventToCalendar(@PathVariable String calendarId, @RequestBody Event event) {
        return calendarService.addEvent(calendarId, event);
    }
    
    @GetMapping("/{username}/calendars")
    public Object getAllCalendar(@PathVariable String username) {
        return calendarService.getAllCalendarsByUsername(username);
    }

    @GetMapping("/{username}/{calendarId}/events")
    public Object getAllEventsByCalendarId(@PathVariable String username, @PathVariable String calendarId) {
        return calendarService.getAllEventsByCalendarId(calendarId);
    }

}
