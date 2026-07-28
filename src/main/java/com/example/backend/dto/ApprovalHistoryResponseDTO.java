package com.example.backend.dto;

public class ApprovalHistoryResponseDTO {

    private Long id;

    private Long documentId;

    private String documentTitle;

    private String status;

    private String actionBy;

    private String actionDate;

    private String remarks;

    public ApprovalHistoryResponseDTO() {
    }

    public ApprovalHistoryResponseDTO(
            Long id,
            Long documentId,
            String documentTitle,
            String status,
            String actionBy,
            String actionDate,
            String remarks) {

        this.id = id;
        this.documentId = documentId;
        this.documentTitle = documentTitle;
        this.status = status;
        this.actionBy = actionBy;
        this.actionDate = actionDate;
        this.remarks = remarks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getDocumentId() {
        return documentId;
    }

    public void setDocumentId(Long documentId) {
        this.documentId = documentId;
    }

    public String getDocumentTitle() {
        return documentTitle;
    }

    public void setDocumentTitle(String documentTitle) {
        this.documentTitle = documentTitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActionBy() {
        return actionBy;
    }

    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }

    public String getActionDate() {
        return actionDate;
    }

    public void setActionDate(String actionDate) {
        this.actionDate = actionDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}