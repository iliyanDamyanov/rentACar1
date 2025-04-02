package org.rentacar1.app.web.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.notification.service.NotificationService;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
public class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NotificationService notificationService;

    @MockitoBean
    private UserService userService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createNotification_shouldReturnCreatedNotification() throws Exception {
        // Arrange
        Notification notification = Notification.builder()
                .id(UUID.randomUUID())
                .recipient("user@example.com")
                .message("Test Message")
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        when(notificationService.createNotification(notification)).thenReturn(notification);


        mockMvc.perform(post("/api/notifications")
                        .contentType(APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(notification)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.recipient").value("user@example.com"))
                .andExpect(jsonPath("$.message").value("Test Message"))
                .andExpect(jsonPath("$.read").value(false));
    }

    @Test
    void getAllNotifications_shouldReturnList() throws Exception {
        Notification notification = Notification.builder()
                .recipient("user@example.com")
                .message("Another Message")
                .createdAt(LocalDateTime.now())
                .read(false)
                .build();

        when(notificationService.getAllNotifications()).thenReturn(List.of(notification));

        mockMvc.perform(get("/api/notifications/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].recipient").value("user@example.com"))
                .andExpect(jsonPath("$[0].message").value("Another Message"));
    }

    @Test
    void getUserNotifications_shouldReturnTextResponse() throws Exception {
        // Arrange
        String username = "user@example.com";
        UUID userId = UUID.randomUUID();
        User mockUser = new User();
        mockUser.setId(userId);
        mockUser.setUsername(username);

        NotificationDTO dto = NotificationDTO.builder()
                .userId(userId)
                .recipient(username)
                .message("Test Message")
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();

        List<NotificationDTO> notifications = List.of(dto);

        when(userService.findByUsername(username)).thenReturn(mockUser);
        when(notificationService.getNotificationsForCurrentUser(userId)).thenReturn(notifications);

        // Act + Assert
        mockMvc.perform(get("/api/notifications/user")
                        .with(csrf())
                        .with(user(username)))
                .andExpect(status().isOk())
                .andExpect(content().string("notifications"));
    }
}
