package com.example.backend.dto;

public class DashboardStatsDTO {

    private long totalUsers;
    private long totalDocuments;
    private long totalCategories;
    private long totalAuditLogs;

    private long draftDocuments;
    private long submittedDocuments;
    private long reviewDocuments;
    private long approvedDocuments;
    private long rejectedDocuments;
    private long archivedDocuments;

    public DashboardStatsDTO() {
    }

    public long getTotalUsers() {
        return totalUsers;
    }

    public void setTotalUsers(long totalUsers) {
        this.totalUsers = totalUsers;
    }

    public long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
        this.totalDocuments = totalDocuments;
    }

    public long getTotalCategories() {
        return totalCategories;
    }

    public void setTotalCategories(long totalCategories) {
        this.totalCategories = totalCategories;
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

    public long getReviewDocuments() {
        return reviewDocuments;
    }

    public void setReviewDocuments(long reviewDocuments) {
        this.reviewDocuments = reviewDocuments;
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
}