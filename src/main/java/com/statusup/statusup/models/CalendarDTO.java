package com.statusup.statusup.models;

public class CalendarDTO {
    private String name;
    private String ownerUsername;

    public CalendarDTO(String name, String ownerUsername) {
        this.name = name;
        this.ownerUsername = ownerUsername;
    }

    public String getName() {
        return name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

}
