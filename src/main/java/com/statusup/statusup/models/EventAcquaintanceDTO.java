package com.statusup.statusup.models;

import java.time.LocalDate;

public class EventAcquaintanceDTO {

    private LocalDate startDate;
    private LocalDate endDate;

    public EventAcquaintanceDTO(LocalDate startDate, LocalDate endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

}
