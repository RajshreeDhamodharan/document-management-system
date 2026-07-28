package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "approval_history")
public class ApprovalHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Document
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    // Workflow Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DocumentStatus status;

    // User who performed the action
    @Column(nullable = false)
    private String actionBy;

    // Action Date
    @Column(nullable = false)
    private String actionDate;

    // Remarks
    @Column(length = 1000)
    private String remarks;

    public ApprovalHistory() {
    }

    public ApprovalHistory(Document document,
                           DocumentStatus status,
                           String actionBy,
                           String actionDate,
                           String remarks) {

        this.document = document;
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

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
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