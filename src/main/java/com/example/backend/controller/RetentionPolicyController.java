package com.example.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.RetentionPolicyDTO;
import com.example.backend.service.RetentionPolicyService;
import com.example.backend.dto.UpcomingExpiryDTO;
@RestController
@RequestMapping("/api/retention/policy")
@CrossOrigin(origins = "http://localhost:3000")
public class RetentionPolicyController {

    private final RetentionPolicyService service;

    public RetentionPolicyController(RetentionPolicyService service) {
        this.service = service;
    }

    // ==========================================
    // Create Policy
    // ==========================================

    @PostMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RetentionPolicyDTO> createPolicy(
            @RequestBody RetentionPolicyDTO dto) {

        return ResponseEntity.ok(service.createPolicy(dto));
    }

    // ==========================================
    // Get All Policies
    // ==========================================

    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
    public ResponseEntity<List<RetentionPolicyDTO>> getAllPolicies() {

        return ResponseEntity.ok(service.getAllPolicies());
    }

    // ==========================================
    // Get Policy By Id
    // ==========================================

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
    public ResponseEntity<RetentionPolicyDTO> getPolicy(
            @PathVariable Long id) {

        return ResponseEntity.ok(service.getPolicy(id));
    }

    // ==========================================
    // Update Policy
    // ==========================================

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<RetentionPolicyDTO> updatePolicy(
            @PathVariable Long id,
            @RequestBody RetentionPolicyDTO dto) {

        return ResponseEntity.ok(service.updatePolicy(id, dto));
    }

    // ==========================================
    // Delete Policy
    // ==========================================

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<String> deletePolicy(
            @PathVariable Long id) {

        service.deletePolicy(id);

        return ResponseEntity.ok("Retention Policy deleted successfully.");
    }
    // ==========================================
// Upcoming Expiry Documents
// ==========================================
// ==========================================
// Upcoming Expiry Documents
// ==========================================
@GetMapping("/upcoming")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
public ResponseEntity<List<UpcomingExpiryDTO>> getUpcomingDocuments() {

    return ResponseEntity.ok(service.getUpcomingDocuments());
}
}