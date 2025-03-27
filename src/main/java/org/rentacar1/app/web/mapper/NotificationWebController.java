package org.rentacar1.app.web.mapper;

import org.rentacar1.app.notification.model.Notification;
import org.rentacar1.app.notification.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/notifications")
public class NotificationWebController {

    private final NotificationService notificationService;

    @Autowired
    public NotificationWebController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping
    public String viewNotifications(Model model) {
        List<Notification> notifications = notificationService.getAllNotifications();
        model.addAttribute("notifications", notifications);
        return "notifications";
    }

    @PostMapping("/delete/{id}")
    public String deleteNotification(@PathVariable UUID id) {
        notificationService.deleteNotification(id);
        return "redirect:/notifications";
    }

}
