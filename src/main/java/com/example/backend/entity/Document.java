package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "documents")
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(length = 1000)
    private String description;

    private String category;

    private String fileName;

    private String filePath;
    // ==========================================
// OCR Extracted Text
// ==========================================

@Column(columnDefinition = "LONGTEXT")
private String extractedText;


    private String uploadedBy;

    private String uploadDate;

    @Enumerated(EnumType.STRING)
    private DocumentStatus status;

    // Workflow Fields
    private String submittedDate;

    private String reviewedDate;

    private String approvedBy;

    private String approvedDate;

    @Column(length = 1000)
    private String remarks;

    private boolean archived;

    // ==========================
    // Default Constructor
    // ==========================
    public Document() {
    }

    // ==========================
    // Parameterized Constructor
    // ==========================
    public Document(Long id,
                String title,
                String description,
                String category,
                String fileName,
                String filePath,
                String extractedText,
                String uploadedBy,
                String uploadDate,
                DocumentStatus status,
                String submittedDate,
                String reviewedDate,
                String approvedBy,
                String approvedDate,
                String remarks,
                boolean archived) {

    this.id = id;
    this.title = title;
    this.description = description;
    this.category = category;
    this.fileName = fileName;
    this.filePath = filePath;
    this.extractedText = extractedText;
    this.uploadedBy = uploadedBy;
    this.uploadDate = uploadDate;
    this.status = status;
    this.submittedDate = submittedDate;
    this.reviewedDate = reviewedDate;
    this.approvedBy = approvedBy;
    this.approvedDate = approvedDate;
    this.remarks = remarks;
    this.archived = archived;
}

    // ==========================
    // Getters and Setters
    // ==========================

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(String uploadDate) {
        this.uploadDate = uploadDate;
    }

    public DocumentStatus getStatus() {
        return status;
    }

    public void setStatus(DocumentStatus status) {
        this.status = status;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public void setSubmittedDate(String submittedDate) {
        this.submittedDate = submittedDate;
    }

    public String getReviewedDate() {
        return reviewedDate;
    }

    public void setReviewedDate(String reviewedDate) {
        this.reviewedDate = reviewedDate;
    }

    public String getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(String approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getApprovedDate() {
        return approvedDate;
    }

    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }
    public String getExtractedText() {
    return extractedText;
   }

  public void setExtractedText(String extractedText) {
    this.extractedText = extractedText;
}
}