package com.example.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentVersion;

@Repository
public interface DocumentVersionRepository
        extends JpaRepository<DocumentVersion, Long> {

    List<DocumentVersion> findByDocumentOrderByVersionDesc(Document document);

    Optional<DocumentVersion> findTopByDocumentOrderByVersionDesc(Document document);

    // Delete all versions for a document
    void deleteByDocument(Document document);
}