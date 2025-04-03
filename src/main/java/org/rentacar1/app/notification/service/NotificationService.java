package org.rentacar1.app.notification.service;

import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class NotificationService {

    private final RestTemplate restTemplate;
    private final String NOTIFICATION_SERVICE_URL = "http://localhost:8081/api/notifications";

    @Autowired
    public NotificationService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Notification createNotification(Notification notification) {
        try {
            ResponseEntity<Notification> response = restTemplate.postForEntity(NOTIFICATION_SERVICE_URL, notification, Notification.class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return response.getBody();
            } else {
                throw new IllegalStateException("Failed to create notification. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error creating notification: " + e.getMessage());
            return null;
        }
    }

    public List<Notification> getAllNotifications() {
        try {
            ResponseEntity<Notification[]> response = restTemplate.getForEntity(NOTIFICATION_SERVICE_URL, Notification[].class);
            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                return List.of(response.getBody());
            } else {
                throw new IllegalStateException("Failed to retrieve notifications. Status code: " + response.getStatusCode());
            }
        } catch (Exception e) {
            System.err.println("Error retrieving notifications: " + e.getMessage());
            return List.of();
        }
    }


    public void deleteNotification(UUID id) {
        try {
            String url = NOTIFICATION_SERVICE_URL + "/" + id;
            restTemplate.delete(url);
            System.out.println("Notification deleted successfully.");
        } catch (Exception e) {
            System.err.println("Error deleting notification: " + e.getMessage());
        }
    }


    public List<NotificationDTO> getNotificationsForCurrentUser(UUID userId) {
        String url = "http://localhost:8081/api/notifications/notifications/user/" + userId;

        ResponseEntity<NotificationDTO[]> response = restTemplate.getForEntity(url, NotificationDTO[].class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return Arrays.asList(response.getBody());
        }
        return Collections.emptyList();
    }
}
