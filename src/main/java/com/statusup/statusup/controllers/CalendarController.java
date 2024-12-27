package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.services.CalendarService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class CalendarController {

    private CalendarService calendarService;

    public CalendarController(CalendarService calendarService) {
        this.calendarService = calendarService;
    }

    @PostMapping("/calendar")
    public String createCalendar(@RequestBody Calendar calendar) {
        return calendarService.createCalendar(calendar);
    }

    @PostMapping("/calendar/{calendarId}/event")
    public String createCalendar(@PathVariable String calendarId, @RequestBody Event event) {
        return calendarService.addEvent(calendarId, event);
    }

    @GetMapping("/calendar/{userId}/{calendarId}")
    public Calendar getCalendar(@PathVariable String userId, @PathVariable String calendarId) {
        return calendarService.getCalendar(userId, calendarId);
    }
    

}
