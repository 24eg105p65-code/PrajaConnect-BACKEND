package com.prajaconnect.repository;

import com.prajaconnect.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    List<Notification> findAllByOrderByIdDesc();
    List<Notification> findByUserId(String userId);
}
