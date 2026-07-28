package com.example.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.ApprovalHistoryResponseDTO;
import com.example.backend.service.ApprovalHistoryService;

@RestController
@RequestMapping("/api/approval-history")
@CrossOrigin(origins = "http://localhost:3000")
public class ApprovalHistoryController {

    private final ApprovalHistoryService approvalHistoryService;

    public ApprovalHistoryController(
            ApprovalHistoryService approvalHistoryService) {

        this.approvalHistoryService = approvalHistoryService;
    }

    // ==========================================
    // Get Complete Approval History
    // ==========================================
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','APPROVER')")
    public List<ApprovalHistoryResponseDTO> getAllHistory() {

        return approvalHistoryService.getAllHistory();
    }

    // ==========================================
    // Get History By Document
    // ==========================================
    @GetMapping("/document/{documentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','APPROVER','EDITOR','VIEWER')")
    public List<ApprovalHistoryResponseDTO> getHistoryByDocument(
            @PathVariable Long documentId) {

        return approvalHistoryService
                .getHistoryByDocument(documentId);
    }

    // ==========================================
    // Get History By User
    // ==========================================
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<ApprovalHistoryResponseDTO> getHistoryByUser(
            @PathVariable String email) {

        return approvalHistoryService
                .getHistoryByUser(email);
    }

}