package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.DashboardResponseDTO;
import com.example.backend.dto.RecentActivityDTO;
import com.example.backend.dto.RecentDocumentDTO;
import com.example.backend.dto.StatisticsResponseDTO;
import com.example.backend.entity.AuditLog;
import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentStatus;
import com.example.backend.entity.Role;
import com.example.backend.repository.AuditLogRepository;
import com.example.backend.repository.CategoryRepository;
import com.example.backend.repository.DocumentRepository;
import com.example.backend.repository.UserRepository;

@Service
public class DashboardService {

    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final DocumentRepository documentRepository;
    private final AuditLogRepository auditLogRepository;

    public DashboardService(UserRepository userRepository,
                            CategoryRepository categoryRepository,
                            DocumentRepository documentRepository,
                            AuditLogRepository auditLogRepository) {

        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.documentRepository = documentRepository;
        this.auditLogRepository = auditLogRepository;
    }

    // ==========================================
    // Dashboard Summary
    // ==========================================

    public DashboardResponseDTO getDashboardSummary() {

        DashboardResponseDTO dashboard = new DashboardResponseDTO();

        // Overall Counts
        dashboard.setTotalUsers(userRepository.count());
        dashboard.setTotalCategories(categoryRepository.count());
        dashboard.setTotalDocuments(documentRepository.count());
        dashboard.setTotalAuditLogs(auditLogRepository.count());

        // Workflow Counts
        dashboard.setDraftDocuments(
                documentRepository.countByStatus(DocumentStatus.DRAFT));

        dashboard.setSubmittedDocuments(
                documentRepository.countByStatus(DocumentStatus.SUBMITTED));

        dashboard.setUnderReviewDocuments(
                documentRepository.countByStatus(DocumentStatus.UNDER_REVIEW));

        dashboard.setApprovedDocuments(
                documentRepository.countByStatus(DocumentStatus.APPROVED));

        dashboard.setRejectedDocuments(
                documentRepository.countByStatus(DocumentStatus.REJECTED));

        dashboard.setArchivedDocuments(
                documentRepository.countByStatus(DocumentStatus.ARCHIVED));

        // Recent Documents
        List<RecentDocumentDTO> recentDocuments =
                documentRepository.findTop5ByOrderByIdDesc()
                        .stream()
                        .map(this::convertDocument)
                        .toList();

        dashboard.setRecentDocuments(recentDocuments);

        // Recent Activities
        List<RecentActivityDTO> recentActivities =
                auditLogRepository.findTop5ByOrderByIdDesc()
                        .stream()
                        .map(this::convertActivity)
                        .toList();

        dashboard.setRecentActivities(recentActivities);

        return dashboard;
    }

    // ==========================================
    // Documents By Status
    // ==========================================

    public List<StatisticsResponseDTO> getDocumentsByStatus() {

        return List.of(

                new StatisticsResponseDTO(
                        "Draft",
                        documentRepository.countByStatus(DocumentStatus.DRAFT)
                ),

                new StatisticsResponseDTO(
                        "Submitted",
                        documentRepository.countByStatus(DocumentStatus.SUBMITTED)
                ),

                new StatisticsResponseDTO(
                        "Under Review",
                        documentRepository.countByStatus(DocumentStatus.UNDER_REVIEW)
                ),

                new StatisticsResponseDTO(
                        "Approved",
                        documentRepository.countByStatus(DocumentStatus.APPROVED)
                ),

                new StatisticsResponseDTO(
                        "Rejected",
                        documentRepository.countByStatus(DocumentStatus.REJECTED)
                ),

                new StatisticsResponseDTO(
                        "Archived",
                        documentRepository.countByStatus(DocumentStatus.ARCHIVED)
                )
        );
    }

    // ==========================================
    // Users By Role
    // ==========================================

    public List<StatisticsResponseDTO> getUsersByRole() {

        return List.of(

                new StatisticsResponseDTO(
                        "SUPER_ADMIN",
                        userRepository.countByRole(Role.SUPER_ADMIN)
                ),

                new StatisticsResponseDTO(
                        "OWNER",
                        userRepository.countByRole(Role.OWNER)
                ),

                new StatisticsResponseDTO(
                        "EDITOR",
                        userRepository.countByRole(Role.EDITOR)
                ),

                new StatisticsResponseDTO(
                        "VIEWER",
                        userRepository.countByRole(Role.VIEWER)
                ),

                new StatisticsResponseDTO(
                        "APPROVER",
                        userRepository.countByRole(Role.APPROVER)
                ),

                new StatisticsResponseDTO(
                        "GUEST",
                        userRepository.countByRole(Role.GUEST)
                )
        );
    }

    // ==========================================
    // Convert Document Entity -> DTO
    // ==========================================

    private RecentDocumentDTO convertDocument(Document document) {

        RecentDocumentDTO dto = new RecentDocumentDTO();

        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setUploadedBy(document.getUploadedBy());
        dto.setUploadDate(document.getUploadDate());
        dto.setStatus(document.getStatus());

        return dto;
    }

    // ==========================================
    // Convert AuditLog Entity -> DTO
    // ==========================================

    private RecentActivityDTO convertActivity(AuditLog log) {

        RecentActivityDTO dto = new RecentActivityDTO();

        dto.setUserEmail(log.getUserEmail());
        dto.setAction(log.getAction());
        dto.setModule(log.getModule());
        dto.setCreatedAt(log.getCreatedAt());

        return dto;
    }
    // ==========================================
// Documents By Category
// ==========================================

public List<StatisticsResponseDTO> getDocumentsByCategory() {

    return documentRepository.countDocumentsByCategory()
            .stream()
            .map(row -> new StatisticsResponseDTO(
                    row[0].toString(),
                    ((Long) row[1]).longValue()))
            .toList();
}
// ==========================================
// Monthly Upload Statistics
// ==========================================

public List<StatisticsResponseDTO> getMonthlyUploads() {

    return documentRepository.countMonthlyUploads()
            .stream()
            .map(row -> new StatisticsResponseDTO(
                    row[0].toString(),
                    ((Long) row[1]).longValue()))
            .toList();
}
}