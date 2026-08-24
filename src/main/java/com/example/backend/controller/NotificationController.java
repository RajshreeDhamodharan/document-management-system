package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.NotificationDTO;
import com.example.backend.service.NotificationService;

@RestController
@RequestMapping("/api/notifications")
@CrossOrigin(origins = "http://localhost:3000")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    // ==========================================
    // Get All Notifications
    // ==========================================

    @GetMapping
    public ResponseEntity<List<NotificationDTO>> getNotifications() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                notificationService.getNotifications(
                        authentication.getName()));
    }

    // ==========================================
    // Get Unread Notifications
    // ==========================================

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDTO>> getUnreadNotifications() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                notificationService.getUnreadNotifications(
                        authentication.getName()));
    }

    // ==========================================
    // Get Unread Count
    // ==========================================

    @GetMapping("/count")
    public ResponseEntity<Long> getUnreadCount() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        return ResponseEntity.ok(
                notificationService.getUnreadCount(
                        authentication.getName()));
    }

    // ==========================================
    // Mark One Notification Read
    // ==========================================

   @PutMapping("/read/{id}")
public ResponseEntity<String> markAsRead(
        @PathVariable Long id) {

    Authentication authentication =
            SecurityContextHolder.getContext().getAuthentication();

    notificationService.markAsRead(
            id,
            authentication.getName());

    return ResponseEntity.ok("Notification marked as read.");
}

    // ==========================================
    // Mark All Read
    // ==========================================

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        notificationService.markAllAsRead(
                authentication.getName());

        return ResponseEntity.ok("All notifications marked as read.");
    }
}