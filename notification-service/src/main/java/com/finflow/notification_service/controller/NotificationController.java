package com.finflow.notification_service.controller;

import com.finflow.notification_service.entity.Notification;
import com.finflow.notification_service.repository.NotificationRepository;
import com.finflow.notification_service.service.NotificationService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {
    private final NotificationService notificationService ;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/{userId}")
    public Page<Notification> getUserNotification(@PathVariable UUID userId,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "10") int size)
    {
        return notificationService.getUserNotifications(userId,page,size);
    }

    @GetMapping("/{userId}/unread-count")
    public long getUnreadCount(@PathVariable UUID userId)
    {
        return notificationService.getUnreadCount(userId);
    }

    @PatchMapping("/{userId}/{notificationId}/read")
    public void markAsRead(@PathVariable UUID userId ,@PathVariable UUID notificationId)
    {
        notificationService.markAsRead(notificationId,userId);
    }
}
