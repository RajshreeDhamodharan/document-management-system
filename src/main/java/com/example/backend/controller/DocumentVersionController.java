package com.example.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.DocumentVersionDTO;
import com.example.backend.service.DocumentVersionService;

@RestController
@RequestMapping("/api/document-versions")
public class DocumentVersionController {

    private final DocumentVersionService versionService;

    public DocumentVersionController(
            DocumentVersionService versionService) {

        this.versionService = versionService;
    }

    // ==========================================
    // Get Version History
    // ==========================================

    @GetMapping("/{documentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<DocumentVersionDTO> getVersions(
            @PathVariable Long documentId) {

        return versionService.getVersions(documentId);
    }
}