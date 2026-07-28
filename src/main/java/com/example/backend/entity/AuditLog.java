package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "audit_logs")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // User who performed the action
    @Column(nullable = false)
    private String userEmail;

    // LOGIN, UPLOAD, DELETE, DOWNLOAD...
    @Column(nullable = false)
    private String action;

    // Authentication, Document, Workflow...
    @Column(nullable = false)
    private String module;

    // Human readable description
    @Column(length = 1000)
    private String description;

    // Client IP Address
    @Column(nullable = false)
    private String ipAddress;

    // Date & Time
    @Column(nullable = false)
    private String createdAt;

    public AuditLog() {
    }

    public AuditLog(
            String userEmail,
            String action,
            String module,
            String description,
            String ipAddress,
            String createdAt) {

        this.userEmail = userEmail;
        this.action = action;
        this.module = module;
        this.description = description;
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}