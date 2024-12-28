package com.statusup.statusup.models;

import java.time.LocalDate;

public class EventFriendDTO {

    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;

    public EventFriendDTO(String name, LocalDate startDate, LocalDate endDate, String description) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
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

}
