package com.finflow.notification_service.repository;

import com.finflow.notification_service.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository<Notification,UUID> {

    Page<Notification> findByUserIdOrderByCreatedAtDesc(UUID userId, Pageable pageable);
    long countByUserIdAndReadFalse(UUID userId);
    Optional<Notification> findByIdAndUserId(UUID id, UUID userId);
}
