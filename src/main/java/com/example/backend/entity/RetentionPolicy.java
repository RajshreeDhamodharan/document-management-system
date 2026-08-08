package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "retention_policies")
public class RetentionPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    // Number of days to retain documents
    private Integer retentionDays;

    // Notify this many days before expiry
    private Integer notificationDays;

    // Whether auto-archive is enabled
    private boolean autoArchive;

    public RetentionPolicy() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Integer getRetentionDays() {
        return retentionDays;
    }

    public void setRetentionDays(Integer retentionDays) {
        this.retentionDays = retentionDays;
    }

    public Integer getNotificationDays() {
        return notificationDays;
    }

    public void setNotificationDays(Integer notificationDays) {
        this.notificationDays = notificationDays;
    }

    public boolean isAutoArchive() {
        return autoArchive;
    }

    public void setAutoArchive(boolean autoArchive) {
        this.autoArchive = autoArchive;
    }
}