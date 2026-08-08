package com.example.backend.service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.backend.dto.RetentionPolicyDTO;
import com.example.backend.dto.UpcomingExpiryDTO;
import com.example.backend.entity.Document;
import com.example.backend.entity.RetentionPolicy;
import com.example.backend.repository.DocumentRepository;
import com.example.backend.repository.RetentionPolicyRepository;

@Service
public class RetentionPolicyService {

    private final RetentionPolicyRepository repository;
    private final DocumentRepository documentRepository;
   public RetentionPolicyService(
        RetentionPolicyRepository repository,
        DocumentRepository documentRepository) {

    this.repository = repository;
    this.documentRepository = documentRepository;
}

    // ==========================================
    // Create Policy
    // ==========================================

    public RetentionPolicyDTO createPolicy(RetentionPolicyDTO dto) {

        RetentionPolicy policy = new RetentionPolicy();

        policy.setCategory(dto.getCategory());
        policy.setRetentionDays(dto.getRetentionDays());
        policy.setNotificationDays(dto.getNotificationDays());
        policy.setAutoArchive(dto.isAutoArchive());

        return convert(repository.save(policy));
    }

    // ==========================================
    // Get All Policies
    // ==========================================

    public List<RetentionPolicyDTO> getAllPolicies() {

        return repository.findAll()

                .stream()

                .map(this::convert)

                .collect(Collectors.toList());
    }

    // ==========================================
    // Get Policy By Id
    // ==========================================

    public RetentionPolicyDTO getPolicy(Long id) {

        RetentionPolicy policy = repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Retention Policy not found"));

        return convert(policy);
    }

    // ==========================================
    // Update Policy
    // ==========================================

    public RetentionPolicyDTO updatePolicy(Long id,
                                           RetentionPolicyDTO dto) {

        RetentionPolicy policy = repository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Retention Policy not found"));

        policy.setCategory(dto.getCategory());
        policy.setRetentionDays(dto.getRetentionDays());
        policy.setNotificationDays(dto.getNotificationDays());
        policy.setAutoArchive(dto.isAutoArchive());

        return convert(repository.save(policy));
    }

    // ==========================================
    // Delete Policy
    // ==========================================

    public void deletePolicy(Long id) {

        repository.deleteById(id);
    }
    // ==========================================
// Upcoming Expiry Documents
// ==========================================

public List<UpcomingExpiryDTO> getUpcomingDocuments() {

    List<Document> documents =
            documentRepository.findByArchivedFalseAndRetentionDateIsNotNull();

    List<UpcomingExpiryDTO> upcomingDocuments = new ArrayList<>();

    LocalDate today = LocalDate.now();

    for (Document document : documents) {

        if (document.getRetentionDate() == null
                || document.getRetentionDate().isBlank()) {
            continue;
        }

        LocalDate expiryDate =
                LocalDate.parse(document.getRetentionDate());

        long daysRemaining =
                ChronoUnit.DAYS.between(today, expiryDate);

        // Show only documents expiring within next 30 days
        if (daysRemaining >= 0 && daysRemaining <= 30) {

            UpcomingExpiryDTO dto = new UpcomingExpiryDTO();

            dto.setId(document.getId());
            dto.setTitle(document.getTitle());
            dto.setCategory(document.getCategory());
            dto.setRetentionDate(document.getRetentionDate());
            dto.setDaysRemaining(daysRemaining);

            upcomingDocuments.add(dto);
        }
    }

    return upcomingDocuments;
}

    // ==========================================
    // DTO Converter
    // ==========================================

    private RetentionPolicyDTO convert(RetentionPolicy policy) {

        RetentionPolicyDTO dto = new RetentionPolicyDTO();

        dto.setId(policy.getId());
        dto.setCategory(policy.getCategory());
        dto.setRetentionDays(policy.getRetentionDays());
        dto.setNotificationDays(policy.getNotificationDays());
        dto.setAutoArchive(policy.isAutoArchive());

        return dto;
    }
}