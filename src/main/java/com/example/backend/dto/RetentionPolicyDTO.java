package com.example.backend.dto;

public class RetentionPolicyDTO {

    private Long id;

    private String category;

    private Integer retentionDays;

    private Integer notificationDays;

    private boolean autoArchive;

    public RetentionPolicyDTO() {
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