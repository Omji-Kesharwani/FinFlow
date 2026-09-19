package com.finflow.notification_service.service;

import com.finflow.notification_service.entity.Notification;
import com.finflow.notification_service.repository.NotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class NotificationService {
    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public Notification createPaymentNotification(
            UUID userId,
            String message
    ) {
        Notification notification = Notification.builder()
                .userId(userId)
                .type("PAYMENT_COMPLETED")
                .message(message)
                .read(false)
                .createdAt(LocalDateTime.now())
                .build();

        return notificationRepository.save(notification);
    }

    public Page<Notification> getUserNotifications(UUID userId,int page , int size)
    {
        Pageable pageable = PageRequest.of(page,size);
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(
                userId,
                pageable
        );
    }

    public long getUnreadCount(UUID userId)
    {
        return notificationRepository.countByUserIdAndReadFalse(userId);
    }

    public void markAsRead(UUID notificationId,UUID userId)
    {
        Notification notification = notificationRepository.findByIdAndUserId(
                notificationId,userId
        ).orElseThrow(()->new RuntimeException("Notification not found"));
        notification.setRead(true);
        notificationRepository.save(notification);
    }

}
