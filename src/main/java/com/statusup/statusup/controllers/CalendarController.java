package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.Calendar;
import com.statusup.statusup.models.Event;
import com.statusup.statusup.services.CalendarService;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public String createCalendar(@RequestBody Calendar calendar) {
        return calendarService.createCalendar(calendar);
    }

    @GetMapping("/{userId}/{calendarId}")
    public Object getCalendar(@PathVariable String userId, @PathVariable String calendarId) {
        return calendarService.getCalendar(userId, calendarId);
    }

    @DeleteMapping("/{userId}/{calendarId}")
    public Object deleteCalendar(@PathVariable String userId, @PathVariable String calendarId) {
        return calendarService.deleteCalendar(userId, calendarId);
    }

    @PutMapping("{userId}/{calendarId}")
    public Object redactCalendar(@PathVariable String userId, @PathVariable String calendarId, @RequestBody Calendar calendar) {
        return calendarService.redactCalendar(userId, calendarId, calendar);
    }

    @PostMapping("/{calendarId}")
    public String addEventToCalendar(@PathVariable String calendarId, @RequestBody Event event) {
        return calendarService.addEvent(calendarId, event);
    }
    

}
