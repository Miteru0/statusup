package com.statusup.statusup.models;

import java.time.LocalDate;

public class EventFriendDTO {

    private String id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public EventFriendDTO(String id, String name, LocalDate startDate, LocalDate endDate, String description) {
        this.id = id;
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
    }

    public EventFriendDTO(Event event) {
        this.id = event.getId();
        this.name = event.getName();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
        this.description = event.getDescription();
    }

    public String getName() {
        return name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getDescription() {
        return description;
    }

    public String getId() {
        return id;
    }

}
