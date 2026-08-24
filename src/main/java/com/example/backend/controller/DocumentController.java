package com.example.backend.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.DocumentRequestDTO;
import com.example.backend.dto.DocumentResponseDTO;
import com.example.backend.dto.DocumentSearchDTO;
import com.example.backend.entity.DocumentStatus;
import com.example.backend.service.DocumentService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // =========================================================
    // GET ALL DOCUMENTS
    // =========================================================
    // Guest can VIEW documents
    @GetMapping
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public List<DocumentResponseDTO> getAllDocuments() {

        return documentService.getAllDocuments();
    }

    // =========================================================
    // GET DOCUMENT BY ID
    // =========================================================
    @GetMapping("/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public DocumentResponseDTO getDocumentById(
            @PathVariable Long id) {

        return documentService.getDocumentById(id);
    }

    // =========================================================
    // PAGINATED DOCUMENTS
    // =========================================================
    @GetMapping("/page")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public ResponseEntity<Page<DocumentResponseDTO>> getDocuments(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                documentService.getDocuments(page, size)
        );
    }

    // =========================================================
    // ADVANCED SEARCH - POST
    // =========================================================
    @PostMapping("/search")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public ResponseEntity<Page<DocumentResponseDTO>> searchDocuments(
            @RequestBody DocumentSearchDTO searchDTO) {

        return ResponseEntity.ok(
                documentService.searchDocuments(searchDTO)
        );
    }

    // =========================================================
    // FULL TEXT SEARCH
    // =========================================================
    @GetMapping("/search")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public ResponseEntity<List<DocumentResponseDTO>> searchDocuments(
            @RequestParam String keyword) {

        return ResponseEntity.ok(
                documentService.searchDocuments(keyword)
        );
    }

    // =========================================================
    // SEARCH BY TITLE
    // =========================================================
    @GetMapping("/title/{title}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public List<DocumentResponseDTO> searchByTitle(
            @PathVariable String title) {

        return documentService.searchByTitle(title);
    }

    // =========================================================
    // SEARCH BY CATEGORY
    // =========================================================
    @GetMapping("/category/{category}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public List<DocumentResponseDTO> searchByCategory(
            @PathVariable String category) {

        return documentService.searchByCategory(category);
    }

    // =========================================================
    // SEARCH BY STATUS
    // =========================================================
    @GetMapping("/status/{status}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public List<DocumentResponseDTO> searchByStatus(
            @PathVariable String status) {

        return documentService.searchByStatus(
                DocumentStatus.valueOf(status.toUpperCase())
        );
    }

    // =========================================================
    // SEARCH BY UPLOADER
    // =========================================================
    @GetMapping("/uploader/{uploadedBy}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public List<DocumentResponseDTO> searchByUploader(
            @PathVariable String uploadedBy) {

        return documentService.searchByUploader(uploadedBy);
    }

    // =========================================================
    // PREVIEW DOCUMENT
    // =========================================================
    @GetMapping("/preview/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public ResponseEntity<Resource> previewDocument(
            @PathVariable Long id) throws IOException {

        Resource resource =
                documentService.previewDocument(id);

        String contentType =
                Files.probeContentType(
                        resource.getFile().toPath()
                );

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" +
                                resource.getFilename() +
                                "\""
                )
                .contentType(
                        MediaType.parseMediaType(contentType)
                )
                .body(resource);
    }

    // =========================================================
    // DOWNLOAD DOCUMENT
    // =========================================================
    @GetMapping("/download/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR',
                'VIEWER',
                'APPROVER',
                'GUEST'
            )
            """)
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id) throws IOException {

        Resource resource =
                documentService.downloadDocument(id);

        String contentType =
                Files.probeContentType(
                        resource.getFile().toPath()
                );

        if (contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() +
                                "\""
                )
                .contentType(
                        MediaType.parseMediaType(contentType)
                )
                .body(resource);
    }

    // =========================================================
    // UPLOAD DOCUMENT
    // =========================================================
    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR'
            )
            """)
    public DocumentResponseDTO uploadDocument(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("category") String category,
            @RequestParam("retentionDate") String retentionDate)
            throws IOException {

        DocumentRequestDTO dto =
                new DocumentRequestDTO();

        dto.setTitle(title);
        dto.setDescription(description);
        dto.setCategory(category);
        dto.setRetentionDate(retentionDate);

        return documentService.uploadDocument(
                file,
                dto
        );
    }

    // =========================================================
    // UPDATE DOCUMENT
    // =========================================================
    @PutMapping("/update/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER',
                'EDITOR'
            )
            """)
    public DocumentResponseDTO updateDocument(
            @PathVariable Long id,
            @RequestBody @Valid DocumentRequestDTO dto) {

        return documentService.updateDocument(
                id,
                dto
        );
    }

    // =========================================================
    // DELETE DOCUMENT
    // =========================================================
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER'
            )
            """)
    public String deleteDocument(
            @PathVariable Long id) {

        documentService.deleteDocument(id);

        return "Document deleted successfully!";
    }

    // =========================================================
    // SUBMIT DOCUMENT
    // =========================================================
    @PutMapping("/{id}/submit")
    @PreAuthorize("""
            hasAnyRole(
                'EDITOR',
                'SUPER_ADMIN'
            )
            """)
    public DocumentResponseDTO submitDocument(
            @PathVariable Long id) {

        return documentService.submitDocument(id);
    }

    // =========================================================
    // START REVIEW
    // =========================================================
    @PutMapping("/{id}/review")
    @PreAuthorize("""
            hasAnyRole(
                'APPROVER',
                'SUPER_ADMIN'
            )
            """)
    public DocumentResponseDTO startReview(
            @PathVariable Long id) {

        return documentService.startReview(id);
    }

    // =========================================================
    // APPROVE DOCUMENT
    // =========================================================
    @PutMapping("/{id}/approve")
    @PreAuthorize("""
            hasAnyRole(
                 'OWNER',
                'APPROVER',
                'SUPER_ADMIN'
            )
            """)
    public DocumentResponseDTO approveDocument(
            @PathVariable Long id,
            @RequestParam String remarks) {

        return documentService.approveDocument(
                id,
                remarks
        );
    }

    // =========================================================
    // REJECT DOCUMENT
    // =========================================================
    @PutMapping("/{id}/reject")
    @PreAuthorize("""
            hasAnyRole(
                'APPROVER',
                'SUPER_ADMIN'
            )
            """)
    public DocumentResponseDTO rejectDocument(
            @PathVariable Long id,
            @RequestParam String remarks) {

        return documentService.rejectDocument(
                id,
                remarks
        );
    }

    // =========================================================
    // ARCHIVE DOCUMENT
    // =========================================================
    @PutMapping("/{id}/archive")
    @PreAuthorize("""
            hasAnyRole(
                'OWNER',
                'SUPER_ADMIN'
            )
            """)
    public DocumentResponseDTO archiveDocument(
            @PathVariable Long id) {

        return documentService.archiveDocument(id);
    }

    // =========================================================
    // RECYCLE BIN
    // =========================================================
    @GetMapping("/recycle-bin")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER'
            )
            """)
    public List<DocumentResponseDTO> getRecycleBin() {

        return documentService.getRecycleBin();
    }

    // =========================================================
    // RESTORE DOCUMENT
    // =========================================================
    @PutMapping("/restore/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER'
            )
            """)
    public DocumentResponseDTO restoreDocument(
            @PathVariable Long id) {

        return documentService.restoreDocument(id);
    }

    // =========================================================
    // PERMANENT DELETE
    // =========================================================
    @DeleteMapping("/permanent/{id}")
    @PreAuthorize("""
            hasAnyRole(
                'SUPER_ADMIN',
                'OWNER'
            )
            """)
    public String permanentDelete(
            @PathVariable Long id) {

        documentService.permanentDelete(id);

        return "Document permanently deleted.";
    }
}