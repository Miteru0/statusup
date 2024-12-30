package com.statusup.statusup.models;

import java.time.LocalDate;

public class EventAcquaintanceDTO {

    private String id;
    private LocalDate startDate;
    private LocalDate endDate;

    public EventAcquaintanceDTO(String id, LocalDate startDate, LocalDate endDate) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public EventAcquaintanceDTO(Event event) {
        this.id = event.getId();
        this.startDate = event.getStartDate();
        this.endDate = event.getEndDate();
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public String getId() {
        return id;
    }

}
