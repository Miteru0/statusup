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

    @GetMapping("/{userId}/{calendarId}/{eventId}")
    public Object getEvent(@PathVariable String userId, @PathVariable String calendarId, @PathVariable String eventId) {
        return eventService.getEvent(userId, calendarId, eventId);
    }

    @DeleteMapping("/{userId}/{calendarId}/{eventId}")
    public Object removeEvent(@PathVariable String userId, @PathVariable String calendarId, @PathVariable String eventId) {
        return eventService.removeEvent(userId, calendarId, eventId);
    }

    @PutMapping("/{userId}/{calendarId}/{eventId}")
    public Object redactEvent(@PathVariable String userId, @PathVariable String calendarId, @PathVariable String eventId, @RequestBody Event newEvent) {
        return eventService.redactEvent(userId, calendarId, eventId, newEvent);
    }

}
