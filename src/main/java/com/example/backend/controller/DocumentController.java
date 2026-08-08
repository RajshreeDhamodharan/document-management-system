package com.example.backend.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.dto.DocumentRequestDTO;
import com.example.backend.dto.DocumentResponseDTO;
import com.example.backend.entity.DocumentStatus;
import com.example.backend.service.DocumentService;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;

import com.example.backend.dto.DocumentSearchDTO;

import java.nio.file.Files;
@RestController
@RequestMapping("/api/documents")
@CrossOrigin(origins = "http://localhost:3000")
@Validated
// ==========================================
// Preview Document
// ==========================================



public class DocumentController {

    private final DocumentService documentService;
    @GetMapping("/recycle-bin")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
public List<DocumentResponseDTO> getRecycleBin() {

    return documentService.getRecycleBin();
}
    @PutMapping("/restore/{id}")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
public DocumentResponseDTO restoreDocument(
        @PathVariable Long id) {

    return documentService.restoreDocument(id);
}
@DeleteMapping("/permanent/{id}")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
public String permanentDelete(
        @PathVariable Long id) {

    documentService.permanentDelete(id);

    return "Document permanently deleted.";
}


    // ==========================================
// Get Documents with Pagination
// ==========================================
@GetMapping("/preview/{id}")
public ResponseEntity<Resource> previewDocument(
        @PathVariable Long id) throws IOException {

    Resource resource = documentService.previewDocument(id);

    String contentType = Files.probeContentType(
            resource.getFile().toPath());

    if (contentType == null) {
        contentType = "application/octet-stream";
    }

    return ResponseEntity.ok()
            .header(HttpHeaders.CONTENT_DISPOSITION,
                    "inline; filename=\"" + resource.getFilename() + "\"")
            .contentType(MediaType.parseMediaType(contentType))
            .body(resource);
}

@GetMapping("/page")
public ResponseEntity<Page<DocumentResponseDTO>> getDocuments(

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            documentService.getDocuments(page, size));
}

    // ==========================================
// Advanced Search
// ==========================================

@PostMapping("/search")
public ResponseEntity<Page<DocumentResponseDTO>> searchDocuments(

        @RequestBody DocumentSearchDTO searchDTO) {

    return ResponseEntity.ok(
            documentService.searchDocuments(searchDTO));
}
    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    // ==========================================
    // Upload Document
    // ==========================================
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR')")
    
public DocumentResponseDTO uploadDocument(

        @RequestParam("file") MultipartFile file,

        @RequestParam("title") String title,

        @RequestParam("description") String description,

        @RequestParam("category") String category,

        @RequestParam("retentionDate") String retentionDate)

        throws IOException {

    DocumentRequestDTO dto = new DocumentRequestDTO();

    dto.setTitle(title);
    dto.setDescription(description);
    dto.setCategory(category);
    dto.setRetentionDate(retentionDate);

    return documentService.uploadDocument(file, dto);
}

    // ==========================================
    // Download Document
    // ==========================================
    @GetMapping("/download/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public ResponseEntity<Resource> downloadDocument(
            @PathVariable Long id)
            throws IOException {

        Resource resource = documentService.downloadDocument(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // ==========================================
    // Get All Documents
    // ==========================================
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<DocumentResponseDTO> getAllDocuments() {

        return documentService.getAllDocuments();
    }
    // ==========================================
// Full Text Search
// ==========================================

@GetMapping("/search")
public ResponseEntity<List<DocumentResponseDTO>> searchDocuments(
        @RequestParam String keyword) {

    return ResponseEntity.ok(
            documentService.searchDocuments(keyword));
}
    // ==========================================
    // Get Document By ID
    // ==========================================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public DocumentResponseDTO getDocumentById(
            @PathVariable Long id) {

        return documentService.getDocumentById(id);
    }

    // ==========================================
    // Update Document
    // ==========================================
    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR')")
    public DocumentResponseDTO updateDocument(

            @PathVariable Long id,

            @RequestBody
            @Valid
            DocumentRequestDTO dto) {

        return documentService.updateDocument(id, dto);
    }

    // ==========================================
    // Delete Document
    // ==========================================
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
    public String deleteDocument(@PathVariable Long id) {

        documentService.deleteDocument(id);

        return "Document deleted successfully!";
    }

    // ==========================================
    // Search By Title
    // ==========================================
    @GetMapping("/title/{title}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<DocumentResponseDTO> searchByTitle(
            @PathVariable String title) {

        return documentService.searchByTitle(title);
    }

    // ==========================================
    // Search By Category
    // ==========================================
    @GetMapping("/category/{category}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<DocumentResponseDTO> searchByCategory(
            @PathVariable String category) {

        return documentService.searchByCategory(category);
    }

    // ==========================================
    // Search By Status
    // ==========================================
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<DocumentResponseDTO> searchByStatus(
            @PathVariable String status) {

        return documentService.searchByStatus(
                DocumentStatus.valueOf(status.toUpperCase()));
    }

    // ==========================================
    // Search By Uploader
    // ==========================================
    @GetMapping("/uploader/{uploadedBy}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<DocumentResponseDTO> searchByUploader(
            @PathVariable String uploadedBy) {

        return documentService.searchByUploader(uploadedBy);
    }

    // ==========================================
    // Submit Document
    // ==========================================
    @PutMapping("/{id}/submit")
    @PreAuthorize("hasAnyRole('EDITOR','SUPER_ADMIN')")
    public DocumentResponseDTO submitDocument(
            @PathVariable Long id) {

        return documentService.submitDocument(id);
    }

    // ==========================================
    // Start Review
    // ==========================================
    @PutMapping("/{id}/review")
    @PreAuthorize("hasAnyRole('APPROVER','SUPER_ADMIN')")
    public DocumentResponseDTO startReview(
            @PathVariable Long id) {

        return documentService.startReview(id);
    }

    // ==========================================
    // Approve Document
    // ==========================================
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasAnyRole('APPROVER','SUPER_ADMIN')")
    public DocumentResponseDTO approveDocument(

            @PathVariable Long id,

            @RequestParam String remarks) {

        return documentService.approveDocument(id, remarks);
    }

    // ==========================================
    // Reject Document
    // ==========================================
    @PutMapping("/{id}/reject")
    @PreAuthorize("hasAnyRole('APPROVER','SUPER_ADMIN')")
    public DocumentResponseDTO rejectDocument(

            @PathVariable Long id,

            @RequestParam String remarks) {

        return documentService.rejectDocument(id, remarks);
    }

    // ==========================================
    // Archive Document
    // ==========================================
    @PutMapping("/{id}/archive")
    @PreAuthorize("hasAnyRole('OWNER','SUPER_ADMIN')")
    public DocumentResponseDTO archiveDocument(
            @PathVariable Long id) {

        return documentService.archiveDocument(id);
    }

}