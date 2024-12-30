package com.statusup.statusup.controllers;

import org.springframework.web.bind.annotation.RestController;

import com.statusup.statusup.models.Event;
import com.statusup.statusup.services.EventService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/event")
public class EventController {

    private EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/{username}/{calendarId}/{eventId}")
    public Object getEvent(@PathVariable String username, @PathVariable String calendarId, @PathVariable String eventId) {
        return eventService.getEvent(calendarId, eventId);
    }

    @DeleteMapping("/{username}/{calendarId}/{eventId}")
    public Object removeEvent(@PathVariable String username, @PathVariable String calendarId, @PathVariable String eventId) {
        return eventService.removeEvent(username, calendarId, eventId);
    }

    @PutMapping("/{username}/{calendarId}/{eventId}")
    public Object redactEvent(@PathVariable String username, @PathVariable String calendarId, @PathVariable String eventId, @RequestBody Event newEvent) {
        return eventService.redactEvent(username, calendarId, eventId, newEvent);
    }

}
