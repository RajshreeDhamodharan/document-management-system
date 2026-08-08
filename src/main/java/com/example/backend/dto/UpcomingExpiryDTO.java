package com.example.backend.dto;

public class UpcomingExpiryDTO {

    private Long id;
    private String title;
    private String category;
    private String retentionDate;
    private long daysRemaining;

    public UpcomingExpiryDTO() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRetentionDate() {
        return retentionDate;
    }

    public void setRetentionDate(String retentionDate) {
        this.retentionDate = retentionDate;
    }

    public long getDaysRemaining() {
        return daysRemaining;
    }

    public void setDaysRemaining(long daysRemaining) {
        this.daysRemaining = daysRemaining;
    }
}