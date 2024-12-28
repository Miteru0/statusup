package com.statusup.statusup.models;

public class ExceptionDTO {
    
    private String name;
    private String description;

    public ExceptionDTO(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

}
