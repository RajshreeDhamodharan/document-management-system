package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.entity.Notification;
import com.example.backend.repository.NotificationRepository;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    // ==========================================
    // Create Notification
    // ==========================================

    public void createNotification(
            String recipient,
            String title,
            String message,
            String type) {

        Notification notification = new Notification();

        notification.setRecipient(recipient);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setType(type);
        notification.setRead(false);
        notification.setCreatedAt(LocalDateTime.now().toString());

        notificationRepository.save(notification);
    }

    // ==========================================
    // Get All Notifications
    // ==========================================

    public List<NotificationDTO> getNotifications(String recipient) {

        return notificationRepository
                .findByRecipientOrderByIdDesc(recipient)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Get Unread Notifications
    // ==========================================

    public List<NotificationDTO> getUnreadNotifications(
            String recipient) {

        return notificationRepository
                .findByRecipientAndIsReadFalseOrderByIdDesc(recipient)
                .stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Count Unread Notifications
    // ==========================================

    public long getUnreadCount(String recipient) {

        return notificationRepository
                .countByRecipientAndIsReadFalse(recipient);
    }

    // ==========================================
    // Mark Notification as Read
    // ==========================================

    public void markAsRead(Long id, String recipient) {

    Notification notification =
            notificationRepository.findById(id)
                    .orElseThrow(() ->
                            new RuntimeException("Notification not found"));

    if (!notification.getRecipient().equals(recipient)) {
        throw new RuntimeException("You are not authorized to access this notification.");
    }

    notification.setRead(true);

    notificationRepository.save(notification);
}

    // ==========================================
    // Mark All as Read
    // ==========================================

    public void markAllAsRead(String recipient) {

        List<Notification> notifications =
                notificationRepository
                        .findByRecipientAndIsReadFalseOrderByIdDesc(recipient);

        for (Notification notification : notifications) {

            notification.setRead(true);
        }

        notificationRepository.saveAll(notifications);
    }

    // ==========================================
    // Entity -> DTO
    // ==========================================

    private NotificationDTO convert(Notification notification) {

        NotificationDTO dto = new NotificationDTO();

        dto.setId(notification.getId());
        dto.setTitle(notification.getTitle());
        dto.setMessage(notification.getMessage());
        dto.setRecipient(notification.getRecipient());
        dto.setType(notification.getType());
        dto.setRead(notification.isRead());
        dto.setCreatedAt(notification.getCreatedAt());

        return dto;
    }
}