package com.example.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.backend.dto.DocumentVersionDTO;
import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentVersion;
import com.example.backend.repository.DocumentRepository;
import com.example.backend.repository.DocumentVersionRepository;

@Service
public class DocumentVersionService {

    private final DocumentVersionRepository versionRepository;
    private final DocumentRepository documentRepository;

    public DocumentVersionService(
            DocumentVersionRepository versionRepository,
            DocumentRepository documentRepository) {

        this.versionRepository = versionRepository;
        this.documentRepository = documentRepository;
    }

    // ==========================================
    // Save Version
    // ==========================================

    public void saveVersion(Document document) {

        int nextVersion = versionRepository
                .findTopByDocumentOrderByVersionDesc(document)
                .map(v -> v.getVersion() + 1)
                .orElse(1);

        DocumentVersion version = new DocumentVersion();

        version.setDocument(document);
        version.setVersion(nextVersion);
        version.setFileName(document.getFileName());
        version.setFilePath(document.getFilePath());
        version.setUploadedBy(document.getUploadedBy());
        version.setUploadDate(document.getUploadDate());

        versionRepository.save(version);
    }

    // ==========================================
    // Get Version History
    // ==========================================

    public List<DocumentVersionDTO> getVersions(Long documentId) {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        return versionRepository
                .findByDocumentOrderByVersionDesc(document)
                .stream()
                .map(this::convert)
                .toList();
    }

    private DocumentVersionDTO convert(DocumentVersion version) {

        DocumentVersionDTO dto = new DocumentVersionDTO();

        dto.setId(version.getId());
        dto.setVersion(version.getVersion());
        dto.setFileName(version.getFileName());
        dto.setUploadedBy(version.getUploadedBy());
        dto.setUploadDate(version.getUploadDate());

        return dto;
    }
}