package com.statusup.statusup.models;

import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "calendars")
public class Calendar {

    @Id
    private String id;
    private String name;
    private String ownerUsername;
    private List<String> eventsIds;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public void setOwnerUsername(String ownerId) {
        this.ownerUsername = ownerId;
    }

    public List<String> getEventsIds() {
        return eventsIds;
    }

    public void setEventsIds(List<String> eventsIds) {
        this.eventsIds = eventsIds;
    }

    public void addEventId(String eventId) {
        eventsIds.add(eventId);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
}
