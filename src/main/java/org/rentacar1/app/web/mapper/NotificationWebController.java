package org.rentacar1.app.web.mapper;

import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.notification.service.NotificationService;
import org.rentacar1.app.user.model.User;
import org.rentacar1.app.user.service.UserService;
import org.rentacar1.app.web.dto.NotificationDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notifications")
public class NotificationWebController {

    private final NotificationService notificationService;
    private final UserService userService;

    @Autowired
    public NotificationWebController(NotificationService notificationService, UserService userService) {
        this.notificationService = notificationService;
        this.userService = userService;
    }


    @GetMapping("/all")
    public String viewAllNotifications(Model model) {
        List<Notification> notifications = notificationService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        return "notifications";
    }


    @GetMapping
    public String viewUserNotifications(Model model, Principal principal) {
        User currentUser = userService.findByUsername(principal.getName());
        UUID userId = currentUser.getId();


        List<NotificationDTO> notifications = notificationService.getNotificationsForCurrentUser(userId);

        model.addAttribute("notifications", notifications);
        return "notifications";
    }


    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }
}
