package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Notification;

@Repository
public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientOrderByIdDesc(String recipient);

    List<Notification> findByRecipientAndIsReadFalseOrderByIdDesc(
            String recipient);

    long countByRecipientAndIsReadFalse(String recipient);
}