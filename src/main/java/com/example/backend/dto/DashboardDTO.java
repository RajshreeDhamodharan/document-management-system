package com.example.backend.dto;

public class DashboardDTO {

    private long totalDocuments;
    private long draftDocuments;
    private long submittedDocuments;
    private long underReviewDocuments;
    private long approvedDocuments;
    private long rejectedDocuments;
    private long archivedDocuments;

    public DashboardDTO() {
    }

    public long getTotalDocuments() {
        return totalDocuments;
    }

    public void setTotalDocuments(long totalDocuments) {
        this.totalDocuments = totalDocuments;
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
}