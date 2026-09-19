package com.finflow.notification_service.event;

public record NotificationEvent(
        String userId,
        String type,
        String message
) {
}