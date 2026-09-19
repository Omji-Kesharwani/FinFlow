package com.finflow.notification_service.repository;

import com.finflow.notification_service.entity.DltEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DltEventRepository extends JpaRepository<DltEvent, UUID> {
}
