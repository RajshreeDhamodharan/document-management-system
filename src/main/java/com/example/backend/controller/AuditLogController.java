package com.example.backend.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.AuditLogResponseDTO;
import com.example.backend.service.AuditLogService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import com.example.backend.entity.AuditLog;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "http://localhost:3000")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }
    @GetMapping("/page")
public ResponseEntity<Page<AuditLog>> getAuditLogs(

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            auditLogService.getAuditLogs(page, size));
}

    // ==========================================
    // Get All Audit Logs
    // ==========================================
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<AuditLogResponseDTO> getAllLogs() {

        return auditLogService.getAllLogs();
    }

    // ==========================================
    // Get Audit Log By ID
    // ==========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public AuditLogResponseDTO getLogById(
            @PathVariable Long id) {

        return auditLogService.getLogById(id);
    }

    // ==========================================
    // Get Logs By User
    // ==========================================
    @GetMapping("/user/{email}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<AuditLogResponseDTO> getLogsByUser(
            @PathVariable String email) {

        return auditLogService.getLogsByUser(email);
    }

    // ==========================================
    // Get Logs By Action
    // ==========================================
    @GetMapping("/action/{action}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<AuditLogResponseDTO> getLogsByAction(
            @PathVariable String action) {

        return auditLogService.getLogsByAction(action);
    }

    // ==========================================
    // Get Logs By Module
    // ==========================================
    @GetMapping("/module/{module}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<AuditLogResponseDTO> getLogsByModule(
            @PathVariable String module) {

        return auditLogService.getLogsByModule(module);
    }

    // ==========================================
    // Delete Audit Log
    // ==========================================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteLog(
            @PathVariable Long id) {

        auditLogService.deleteLog(id);

        return "Audit Log deleted successfully.";
    }

}