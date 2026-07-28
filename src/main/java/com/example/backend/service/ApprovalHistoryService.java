package com.example.backend.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.ApprovalHistoryResponseDTO;
import com.example.backend.entity.ApprovalHistory;
import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentStatus;
import com.example.backend.repository.ApprovalHistoryRepository;

@Service
public class ApprovalHistoryService {

    private final ApprovalHistoryRepository approvalHistoryRepository;

    public ApprovalHistoryService(
            ApprovalHistoryRepository approvalHistoryRepository) {

        this.approvalHistoryRepository = approvalHistoryRepository;
    }

    // ==========================================
    // Save Workflow History
    // ==========================================
    public void saveHistory(
            Document document,
            DocumentStatus status,
            String actionBy,
            String remarks) {

        ApprovalHistory history = new ApprovalHistory();

        history.setDocument(document);
        history.setStatus(status);
        history.setActionBy(actionBy);
        history.setActionDate(LocalDate.now().toString());
        history.setRemarks(remarks);

        approvalHistoryRepository.save(history);
    }

    // ==========================================
    // Get All History
    // ==========================================
    public List<ApprovalHistoryResponseDTO> getAllHistory() {

        return approvalHistoryRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Get History By Document
    // ==========================================
    public List<ApprovalHistoryResponseDTO>
            getHistoryByDocument(Long documentId) {

        return approvalHistoryRepository
                .findByDocumentIdOrderByActionDateDesc(documentId)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    // ==========================================
    // Get History By User
    // ==========================================
    public List<ApprovalHistoryResponseDTO>
            getHistoryByUser(String email) {

        return approvalHistoryRepository
                .findByActionBy(email)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }
        // ==========================================
    // Entity → DTO
    // ==========================================
    private ApprovalHistoryResponseDTO convertToDTO(
            ApprovalHistory history) {

        ApprovalHistoryResponseDTO dto =
                new ApprovalHistoryResponseDTO();

        dto.setId(history.getId());

        dto.setDocumentId(
                history.getDocument().getId());

        dto.setDocumentTitle(
                history.getDocument().getTitle());

        dto.setStatus(
                history.getStatus().name());

        dto.setActionBy(
                history.getActionBy());

        dto.setActionDate(
                history.getActionDate());

        dto.setRemarks(
                history.getRemarks());

        return dto;
    }

}