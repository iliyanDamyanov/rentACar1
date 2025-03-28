package org.rentacar1.app.web.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationDTO {

    private UUID userId;
    private String recipient;
    private String message;
    private boolean isRead;        // Добавено поле за състояние на нотификацията (прочетена или не)
    private LocalDateTime createdAt; // Добавено поле за дата на създаване

    public NotificationDTO() {
    }

    public NotificationDTO(UUID userId, String recipient, String message, boolean isRead, LocalDateTime createdAt) {
        this.userId = userId;
        this.recipient = recipient;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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
