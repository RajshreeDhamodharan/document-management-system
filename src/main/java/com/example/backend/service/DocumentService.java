package com.example.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.DocumentRequestDTO;
import com.example.backend.dto.DocumentResponseDTO;
import com.example.backend.dto.DocumentSearchDTO;
import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentStatus;
import com.example.backend.repository.DocumentRepository;
import com.example.backend.specification.DocumentSpecification;

@Service
public class DocumentService {

    @Value("${file.upload-dir}")
    private String uploadDir;

    private final DocumentRepository documentRepository;
    private final ApprovalHistoryService approvalHistoryService;
    private final EmailService emailService;
    private final DocumentVersionService documentVersionService;

    public DocumentService(
            DocumentRepository documentRepository,
            ApprovalHistoryService approvalHistoryService,
            EmailService emailService,
            DocumentVersionService documentVersionService) {

        this.documentRepository = documentRepository;
        this.approvalHistoryService = approvalHistoryService;
        this.emailService = emailService;
        this.documentVersionService = documentVersionService;
    }

    // ==========================================
    // Upload Document
    // ==========================================

    public DocumentResponseDTO uploadDocument(
            MultipartFile file,
            DocumentRequestDTO dto)
            throws IOException {

        Files.createDirectories(Paths.get(uploadDir));

        String fileName = file.getOriginalFilename();

        Path filePath = Paths.get(uploadDir, fileName);

        Files.copy(
                file.getInputStream(),
                filePath,
                StandardCopyOption.REPLACE_EXISTING);

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String uploadedBy = authentication.getName();

        Document document = new Document();

        document.setTitle(dto.getTitle());
        document.setDescription(dto.getDescription());
        document.setCategory(dto.getCategory());

        document.setFileName(fileName);
        document.setFilePath(filePath.toString());

        document.setUploadedBy(uploadedBy);
        document.setUploadDate(LocalDate.now().toString());

        document.setStatus(DocumentStatus.DRAFT);
        document.setArchived(false);

        Document savedDocument =
                documentRepository.save(document);

        // Version 1
        documentVersionService.saveVersion(savedDocument);

        // Workflow History
        approvalHistoryService.saveHistory(
                savedDocument,
                DocumentStatus.DRAFT,
                uploadedBy,
                "Document uploaded");

        return convertToResponse(savedDocument);
    }

    // ==========================================
    // Get All Documents
    // ==========================================

    public List<DocumentResponseDTO> getAllDocuments() {

        return documentRepository.findByArchivedFalse()

                .stream()

                .map(this::convertToResponse)

                .collect(Collectors.toList());
    }

    // ==========================================
    // Get Document By Id
    // ==========================================

    public DocumentResponseDTO getDocumentById(Long id) {

        Document document = documentRepository.findById(id)

                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        return convertToResponse(document);
    }
        // ==========================================
    // Update Document
    // ==========================================

    public DocumentResponseDTO updateDocument(
            Long id,
            DocumentRequestDTO dto) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        // Save current version before updating
        documentVersionService.saveVersion(document);

        document.setTitle(dto.getTitle());
        document.setDescription(dto.getDescription());
        document.setCategory(dto.getCategory());

        Document updatedDocument =
                documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                updatedDocument.getStatus(),
                updatedDocument.getUploadedBy(),
                "Document updated");

        return convertToResponse(updatedDocument);
    }

    // ==========================================
    // Soft Delete (Move to Recycle Bin)
    // ==========================================

    public void deleteDocument(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        document.setArchived(true);

        documentRepository.save(document);

        approvalHistoryService.saveHistory(
                document,
                document.getStatus(),
                document.getUploadedBy(),
                "Moved to recycle bin");
    }

    // ==========================================
    // Download Document
    // ==========================================

    public Resource downloadDocument(Long id)
            throws IOException {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        Path path = Paths.get(document.getFilePath());

        Resource resource = new UrlResource(path.toUri());

        if (!resource.exists()) {
            throw new RuntimeException("File not found");
        }

        return resource;
    }

    // ==========================================
    // Preview Document
    // ==========================================

    public Resource previewDocument(Long id)
            throws IOException {

        return downloadDocument(id);
    }
        // ==========================================
    // Search By Title
    // ==========================================

    public List<DocumentResponseDTO> searchByTitle(String title) {

        return documentRepository
                .findByTitleContainingIgnoreCase(title)
                .stream()
                .filter(document -> !document.isArchived())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Search By Category
    // ==========================================

    public List<DocumentResponseDTO> searchByCategory(String category) {

        return documentRepository
                .findByCategory(category)
                .stream()
                .filter(document -> !document.isArchived())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Search By Status
    // ==========================================

    public List<DocumentResponseDTO> searchByStatus(DocumentStatus status) {

        return documentRepository
                .findByStatus(status)
                .stream()
                .filter(document -> !document.isArchived())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Search By Uploaded By
    // ==========================================

    public List<DocumentResponseDTO> searchByUploader(String uploadedBy) {

        return documentRepository
                .findByUploadedBy(uploadedBy)
                .stream()
                .filter(document -> !document.isArchived())
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Pagination
    // ==========================================

    public Page<DocumentResponseDTO> getDocuments(
            int page,
            int size) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by("id").descending());

        Specification<Document> specification =
                (root, query, cb) ->
                        cb.isFalse(root.get("archived"));

        Page<Document> documents =
                documentRepository.findAll(specification, pageable);

        return documents.map(this::convertToResponse);
    }

    // ==========================================
    // Advanced Search
    // ==========================================

    public Page<DocumentResponseDTO> searchDocuments(
            DocumentSearchDTO searchDTO) {

        Pageable pageable = PageRequest.of(
                searchDTO.getPage(),
                searchDTO.getSize(),
                Sort.by(
                        Sort.Direction.fromString(
                                searchDTO.getSortDirection()),
                        searchDTO.getSortBy()));

        Specification<Document> specification =
                DocumentSpecification.search(searchDTO);

        // Exclude archived documents
        specification = specification.and(
                (root, query, cb) ->
                        cb.isFalse(root.get("archived")));

        Page<Document> result =
                documentRepository.findAll(specification, pageable);

        return result.map(this::convertToResponse);
    }
        // ==========================================
    // Submit Document
    // ==========================================

    public DocumentResponseDTO submitDocument(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        if (document.getStatus() != DocumentStatus.DRAFT) {
            throw new RuntimeException("Only DRAFT documents can be submitted.");
        }

        document.setStatus(DocumentStatus.SUBMITTED);
        document.setSubmittedDate(LocalDate.now().toString());

        Document updatedDocument = documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                DocumentStatus.SUBMITTED,
                updatedDocument.getUploadedBy(),
                "Document submitted for review");

        emailService.sendSubmissionEmail(
                updatedDocument.getUploadedBy(),
                updatedDocument.getTitle());

        return convertToResponse(updatedDocument);
    }

    // ==========================================
    // Start Review
    // ==========================================

    public DocumentResponseDTO startReview(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        if (document.getStatus() != DocumentStatus.SUBMITTED) {
            throw new RuntimeException("Only submitted documents can be reviewed.");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String reviewer = authentication.getName();

        document.setStatus(DocumentStatus.UNDER_REVIEW);
        document.setReviewedDate(LocalDate.now().toString());

        Document updatedDocument = documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                DocumentStatus.UNDER_REVIEW,
                reviewer,
                "Document review started");

        emailService.sendReviewEmail(
                updatedDocument.getUploadedBy(),
                updatedDocument.getTitle());

        return convertToResponse(updatedDocument);
    }

    // ==========================================
    // Approve Document
    // ==========================================

    public DocumentResponseDTO approveDocument(
            Long id,
            String remarks) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        if (document.getStatus() != DocumentStatus.UNDER_REVIEW) {
            throw new RuntimeException("Document is not under review.");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String approver = authentication.getName();

        document.setStatus(DocumentStatus.APPROVED);
        document.setApprovedBy(approver);
        document.setApprovedDate(LocalDate.now().toString());
        document.setRemarks(remarks);

        Document updatedDocument = documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                DocumentStatus.APPROVED,
                approver,
                remarks);

        emailService.sendApprovalEmail(
                updatedDocument.getUploadedBy(),
                updatedDocument.getTitle());

        return convertToResponse(updatedDocument);
    }

    // ==========================================
    // Reject Document
    // ==========================================

    public DocumentResponseDTO rejectDocument(
            Long id,
            String remarks) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        if (document.getStatus() != DocumentStatus.UNDER_REVIEW) {
            throw new RuntimeException("Document is not under review.");
        }

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String reviewer = authentication.getName();

        document.setStatus(DocumentStatus.REJECTED);
        document.setRemarks(remarks);

        Document updatedDocument = documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                DocumentStatus.REJECTED,
                reviewer,
                remarks);

        emailService.sendRejectionEmail(
                updatedDocument.getUploadedBy(),
                updatedDocument.getTitle(),
                remarks);

        return convertToResponse(updatedDocument);
    }

    // ==========================================
    // Archive Document
    // ==========================================

    public DocumentResponseDTO archiveDocument(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        if (document.getStatus() != DocumentStatus.APPROVED) {
            throw new RuntimeException("Only approved documents can be archived.");
        }

        document.setStatus(DocumentStatus.ARCHIVED);
        document.setArchived(true);

        Document updatedDocument = documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                DocumentStatus.ARCHIVED,
                updatedDocument.getUploadedBy(),
                "Document archived");

        emailService.sendArchiveEmail(
                updatedDocument.getUploadedBy(),
                updatedDocument.getTitle());

        return convertToResponse(updatedDocument);
    }
        // ==========================================
    // Get Recycle Bin
    // ==========================================

    public List<DocumentResponseDTO> getRecycleBin() {

        return documentRepository
                .findByArchivedTrue()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    // ==========================================
    // Restore Document
    // ==========================================

    public DocumentResponseDTO restoreDocument(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        if (!document.isArchived()) {
            throw new RuntimeException("Document is not in recycle bin.");
        }

        document.setArchived(false);

        // Restore as Draft
        document.setStatus(DocumentStatus.DRAFT);

        Document updatedDocument =
                documentRepository.save(document);

        approvalHistoryService.saveHistory(
                updatedDocument,
                DocumentStatus.DRAFT,
                updatedDocument.getUploadedBy(),
                "Document restored from recycle bin");

        return convertToResponse(updatedDocument);
    }

    // ==========================================
    // Permanent Delete
    // ==========================================

    public void permanentDelete(Long id) {

        Document document = documentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        // Delete physical file if exists
        try {

            Path filePath = Paths.get(document.getFilePath());

            Files.deleteIfExists(filePath);

        } catch (IOException e) {

            System.out.println("Unable to delete file : "
                    + e.getMessage());

        }

        documentRepository.delete(document);
    }
        // ==========================================
    // Convert Entity -> DTO
    // ==========================================

    private DocumentResponseDTO convertToResponse(Document document) {

        DocumentResponseDTO dto = new DocumentResponseDTO();

        dto.setId(document.getId());
        dto.setTitle(document.getTitle());
        dto.setDescription(document.getDescription());
        dto.setCategory(document.getCategory());

        dto.setFileName(document.getFileName());
        dto.setFilePath(document.getFilePath());

        dto.setUploadedBy(document.getUploadedBy());
        dto.setUploadDate(document.getUploadDate());

        dto.setStatus(document.getStatus());

        dto.setSubmittedDate(document.getSubmittedDate());
        dto.setReviewedDate(document.getReviewedDate());

        dto.setApprovedBy(document.getApprovedBy());
        dto.setApprovedDate(document.getApprovedDate());

        dto.setRemarks(document.getRemarks());

        dto.setArchived(document.isArchived());

        return dto;
    }

}