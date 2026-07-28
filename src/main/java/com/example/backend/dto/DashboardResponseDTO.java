package com.example.backend.dto;

import java.util.List;

public class DashboardResponseDTO {

    // ==========================
    // Overall Counts
    // ==========================

    private long totalUsers;
    private long totalCategories;
    private long totalDocuments;
    private long totalAuditLogs;

    // ==========================
    // Workflow Statistics
    // ==========================

    private long draftDocuments;
    private long submittedDocuments;
    private long underReviewDocuments;
    private long approvedDocuments;
    private long rejectedDocuments;
    private long archivedDocuments;

    // ==========================
    // Dashboard Widgets
    // ==========================

    private List<RecentDocumentDTO> recentDocuments;
    private List<RecentActivityDTO> recentActivities;

    // ==========================
    // Default Constructor
    // ==========================

    public DashboardResponseDTO() {
    }

    // ==========================
    // Getters and Setters
    // ==========================

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
    }

    public long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public long getTotalAuditLogs() {
        return totalAuditLogs;
    }

    public void setTotalAuditLogs(long totalAuditLogs) {
        this.totalAuditLogs = totalAuditLogs;
    }

    public long getDraftDocuments() {
        return draftDocuments;
    }

    public void setDraftDocuments(long draftDocuments) {
        this.draftDocuments = draftDocuments;
    }

    public long getSubmittedDocuments() {
        return submittedDocuments;
    }

    public void setSubmittedDocuments(long submittedDocuments) {
        this.submittedDocuments = submittedDocuments;
    }

    public long getUnderReviewDocuments() {
        return underReviewDocuments;
    }

    public void setUnderReviewDocuments(long underReviewDocuments) {
        this.underReviewDocuments = underReviewDocuments;
    }

    public long getApprovedDocuments() {
        return approvedDocuments;
    }

    public void setApprovedDocuments(long approvedDocuments) {
        this.approvedDocuments = approvedDocuments;
    }

    public long getRejectedDocuments() {
        return rejectedDocuments;
    }

    public void setRejectedDocuments(long rejectedDocuments) {
        this.rejectedDocuments = rejectedDocuments;
    }

    public long getArchivedDocuments() {
        return archivedDocuments;
    }

    public void setArchivedDocuments(long archivedDocuments) {
        this.archivedDocuments = archivedDocuments;
    }

    public List<RecentDocumentDTO> getRecentDocuments() {
        return recentDocuments;
    }

    public void setRecentDocuments(List<RecentDocumentDTO> recentDocuments) {
        this.recentDocuments = recentDocuments;
    }

    public List<RecentActivityDTO> getRecentActivities() {
        return recentActivities;
    }

    public void setRecentActivities(List<RecentActivityDTO> recentActivities) {
        this.recentActivities = recentActivities;
    }

}