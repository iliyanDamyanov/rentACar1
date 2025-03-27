package org.rentacar1.app.web.mapper;

import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.notification.service.NotificationService;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {


    private final UserService userService;
    private final NotificationService notificationService;

    @Autowired
    public NotificationController(UserService userService, NotificationService notificationService) {
        this.userService = userService;
        this.notificationService = notificationService;
    }

    @PostMapping
    public ResponseEntity<Notification> createNotification(@RequestBody Notification notification) {
        Notification createdNotification = notificationService.createNotification(notification);
        return ResponseEntity.ok(createdNotification);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Notification>> getAllNotifications() {
        return ResponseEntity.ok(notificationService.getAllNotifications());
    }

    @GetMapping("/user")
    public String getUserNotifications(Model model, Principal principal) {

        User currentUser = userService.findByUsername(principal.getName());
        UUID userId = currentUser.getId();


        List<NotificationDTO> notifications = notificationService.getNotificationsForCurrentUser(userId);

        model.addAttribute("notifications", notifications);

        return "notifications";
    }
}
