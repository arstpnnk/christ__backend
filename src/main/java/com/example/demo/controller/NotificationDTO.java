package com.example.demo.controller;

import com.example.demo.model.Notification;

import java.time.LocalDateTime;

public class NotificationDTO {
    private Long id;
    private String message;
    private String type;
    private boolean isRead;
    private LocalDateTime createdAt;

    public NotificationDTO(Notification notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.type = notification.getType();
        this.isRead = notification.isRead();
        this.createdAt = notification.getCreatedAt();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
