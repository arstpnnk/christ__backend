package com.example.demo.model;

public class Holiday {
    private String date;
    private String title;
    private String description;

    public Holiday(String date, String title, String description) {
        this.date = date;
        this.title = title;
        this.description = description;
    }

    // Getters and setters
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
