package com.example.backend.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.AuditLogResponseDTO;
import com.example.backend.entity.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import com.example.backend.repository.AuditLogRepository;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    // ==========================================
// Pagination
// ==========================================

public Page<AuditLog> getAuditLogs(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    return auditLogRepository.findAll(pageable);
}

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    // ==========================================
    // Save Audit Log
    // ==========================================
    public void saveLog(
            String userEmail,
            String action,
            String module,
            String description,
            String ipAddress) {

        AuditLog log = new AuditLog();

        log.setUserEmail(userEmail);
        log.setAction(action);
        log.setModule(module);
        log.setDescription(description);
        log.setIpAddress(ipAddress);
        log.setCreatedAt(LocalDateTime.now().toString());

        auditLogRepository.save(log);
    }

    // ==========================================
    // Get All Logs
    // ==========================================
    public List<AuditLogResponseDTO> getAllLogs() {

        return auditLogRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Get Audit Log By ID
    // ==========================================
    public AuditLogResponseDTO getLogById(Long id) {

        AuditLog log = auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Audit Log not found with ID : " + id));

        return convertToDTO(log);
    }

    // ==========================================
    // Get Logs By User
    // ==========================================
    public List<AuditLogResponseDTO> getLogsByUser(String email) {

        return auditLogRepository
                .findByUserEmailOrderByCreatedAtDesc(email)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Get Logs By Action
    // ==========================================
    public List<AuditLogResponseDTO> getLogsByAction(String action) {

        return auditLogRepository
                .findByActionOrderByCreatedAtDesc(action)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Get Logs By Module
    // ==========================================
    public List<AuditLogResponseDTO> getLogsByModule(String module) {

        return auditLogRepository
                .findByModuleOrderByCreatedAtDesc(module)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Delete Audit Log
    // ==========================================
    public void deleteLog(Long id) {

        AuditLog log = auditLogRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Audit Log not found with ID : " + id));

        auditLogRepository.delete(log);
    }

    // ==========================================
    // Convert Entity To DTO
    // ==========================================
    private AuditLogResponseDTO convertToDTO(AuditLog log) {

        AuditLogResponseDTO dto = new AuditLogResponseDTO();

        dto.setId(log.getId());
        dto.setUserEmail(log.getUserEmail());
        dto.setAction(log.getAction());
        dto.setModule(log.getModule());
        dto.setDescription(log.getDescription());
        dto.setIpAddress(log.getIpAddress());
        dto.setCreatedAt(log.getCreatedAt());

        return dto;
    }

}