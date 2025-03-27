package org.rentacar1.app.notification.model;


import java.time.LocalDateTime;
import java.util.UUID;



public class Notification {
    private UUID id;
    private String recipient;
    private String message;
    private LocalDateTime createdAt;
    private boolean read; // Добавяме това поле

    public Notification() {
    }

    public Notification(UUID id, String recipient, String message, LocalDateTime createdAt, boolean read) {
        this.id = id;
        this.recipient = recipient;
        this.message = message;
        this.createdAt = createdAt;
        this.read = read;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}