package com.example.backend.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentStatus;
import com.example.backend.entity.RetentionPolicy;
import com.example.backend.repository.DocumentRepository;
import com.example.backend.repository.RetentionPolicyRepository;

@Component
public class RetentionScheduler {

    private final DocumentRepository documentRepository;
    private final RetentionPolicyRepository retentionRepository;
    private final EmailService emailService;
    private final NotificationService notificationService;

    public RetentionScheduler(
            DocumentRepository documentRepository,
            RetentionPolicyRepository retentionRepository,
            EmailService emailService,
            NotificationService notificationService) {

        this.documentRepository = documentRepository;
        this.retentionRepository = retentionRepository;
        this.emailService = emailService;
        this.notificationService = notificationService;
    }

    // ==========================================
    // Runs every day at 09:00 AM
    // ==========================================

@Scheduled(cron = "0 0 9 * * *")
    public void checkRetentionPolicy() {
         System.out.println("====================================");
    System.out.println("Retention Scheduler Executed");
    System.out.println("Time : " + LocalDateTime.now());
    System.out.println("====================================");

        List<Document> documents =
                documentRepository.findByArchivedFalse();

        LocalDate today = LocalDate.now();

        for (Document document : documents) {

            if (document.getRetentionDate() == null
                    || document.getRetentionDate().isBlank()) {
                continue;
            }

          String date = document.getRetentionDate().replace("/", "-");

          LocalDate retentionDate = LocalDate.parse(date);

            RetentionPolicy policy =
                    retentionRepository
                            .findByCategory(document.getCategory())
                            .orElse(null);

            if (policy == null) {
                continue;
            }

            long daysRemaining =
                    ChronoUnit.DAYS.between(today, retentionDate);

            // ==========================================
            // Retention Reminder
            // ==========================================

          // ==========================================
// Retention Reminder
// ==========================================

if (daysRemaining == policy.getNotificationDays()
        && !document.isExpiryNotificationSent()) {

    // Send Email
    emailService.sendRetentionReminder(document);

    // Create Notification
    notificationService.createNotification(
            document.getUploadedBy(),
            "Retention Reminder",
            "Your document \"" + document.getTitle()
                    + "\" will expire on "
                    + document.getRetentionDate(),
            "RETENTION");

    // Prevent duplicate reminders
    document.setExpiryNotificationSent(true);

    documentRepository.save(document);
}
            // ==========================================
            // Auto Archive
            // ==========================================

            if (!today.isBefore(retentionDate)
                    && policy.isAutoArchive()) {

                document.setStatus(DocumentStatus.ARCHIVED);
                document.setArchived(true);

                documentRepository.save(document);

                // In-App Notification
                notificationService.createNotification(
                        document.getUploadedBy(),
                        "Document Auto Archived",
                        "Your document \"" + document.getTitle()
                                + "\" has been automatically archived.",
                        "AUTO_ARCHIVE");

                // Email Notification
                emailService.sendArchiveEmail(
                        document.getUploadedBy(),
                        document.getTitle());
            }
        }
    }
}