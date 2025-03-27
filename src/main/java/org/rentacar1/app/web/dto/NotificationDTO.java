package org.rentacar1.app.web.dto;

import java.util.UUID;

public class NotificationDTO {

    private UUID userId;
    private String recipient;
    private String message;

    public NotificationDTO() {
    }

    public NotificationDTO(UUID userId, String recipient, String message) {
        this.userId = userId;
        this.recipient = recipient;
        this.message = message;
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
}
