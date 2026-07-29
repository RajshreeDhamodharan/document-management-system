package com.example.backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.dto.DigitalSignatureResponseDTO;
import com.example.backend.service.DigitalSignatureService;

@RestController
@RequestMapping("/api/signature")
@CrossOrigin(origins = "http://localhost:3000")
public class DigitalSignatureController {

    private final DigitalSignatureService digitalSignatureService;

    public DigitalSignatureController(
            DigitalSignatureService digitalSignatureService) {

        this.digitalSignatureService = digitalSignatureService;
    }

    // ==========================================
    // Get Digital Signature Details
    // ==========================================

    @GetMapping("/{documentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','APPROVER','VIEWER')")
    public DigitalSignatureResponseDTO getSignature(
            @PathVariable Long documentId)
            throws Exception {

        return digitalSignatureService.getSignature(documentId);
    }

    // ==========================================
    // Verify Digital Signature
    // ==========================================

    @GetMapping("/verify/{documentId}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','APPROVER','VIEWER')")
    public String verifySignature(
            @PathVariable Long documentId)
            throws Exception {

        boolean valid =
                digitalSignatureService.verifySignature(documentId);

        if (valid) {
            return "Digital Signature Verified Successfully";
        }

        return "Digital Signature Verification Failed";
    }
}