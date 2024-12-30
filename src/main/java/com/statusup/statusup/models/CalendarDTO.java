package com.statusup.statusup.models;


public class CalendarDTO {
    private String id;
    private String name;
    private String ownerUsername;

    public CalendarDTO(String id, String name, String ownerUsername) {
        this.id = id;
        this.name = name;
        this.ownerUsername = ownerUsername;
    }
    public CalendarDTO(Calendar calendar) {
    this.id = calendar.getId();
    this.name = calendar.getName();
    this.ownerUsername = calendar.getOwnerUsername();
}

    public String getName() {
        return name;
    }

    public String getOwnerUsername() {
        return ownerUsername;
    }

    public String getId() {
        return id;
    }

}
