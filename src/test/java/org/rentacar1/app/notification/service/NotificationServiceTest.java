package org.rentacar1.app.notification.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class NotificationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private NotificationService notificationService;

    private Notification notification;
    private UUID notificationId;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        notificationId = UUID.randomUUID();
        userId = UUID.randomUUID();
        notification = Notification.builder()
                .id(notificationId)
                .recipient("testUser")
                .message("Test message")
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();
    }

    @Test
    void testCreateNotification_ShouldReturnNotification() {
        when(restTemplate.postForEntity(anyString(), eq(notification), eq(Notification.class)))
                .thenReturn(new ResponseEntity<>(notification, HttpStatus.OK));

        Notification result = notificationService.createNotification(notification);

        assertNotNull(result);
        assertEquals(notification.getMessage(), result.getMessage());
    }

    @Test
    void testGetAllNotifications_ShouldReturnList() {
        Notification[] notifications = {notification};
        when(restTemplate.getForEntity(anyString(), eq(Notification[].class)))
                .thenReturn(new ResponseEntity<>(notifications, HttpStatus.OK));

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getRecipient());
    }

    @Test
    void testDeleteNotification_ShouldExecuteWithoutException() {
        doNothing().when(restTemplate).delete(anyString());

        assertDoesNotThrow(() -> notificationService.deleteNotification(notificationId));
        verify(restTemplate, times(1)).delete(contains(notificationId.toString()));
    }

    @Test
    void testGetNotificationsForCurrentUser_ShouldReturnDTOList() {
        NotificationDTO dto = new NotificationDTO(userId, "testUser", "Message", false, LocalDateTime.now());
        NotificationDTO[] dtos = {dto};

        when(restTemplate.getForEntity(anyString(), eq(NotificationDTO[].class)))
                .thenReturn(new ResponseEntity<>(dtos, HttpStatus.OK));

        List<NotificationDTO> result = notificationService.getNotificationsForCurrentUser(userId);

        assertEquals(1, result.size());
        assertEquals("testUser", result.get(0).getRecipient());
    }
}