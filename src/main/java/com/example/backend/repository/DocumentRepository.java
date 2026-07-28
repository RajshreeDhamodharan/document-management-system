package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentStatus;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>,
        JpaSpecificationExecutor<Document> {

    // ==========================================
    // Basic Search Methods
    // ==========================================

    List<Document> findByTitleContainingIgnoreCase(String title);

    List<Document> findByCategory(String category);

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByUploadedBy(String uploadedBy);
    
List<Document> findByArchivedTrue();

List<Document> findByArchivedFalse();

    // ==========================================
    // Dashboard Methods
    // ==========================================
    // ==========================================
// Recycle Bin
// ==========================================


    long countByStatus(DocumentStatus status);

    long countByArchived(boolean archived);

    List<Document> findTop5ByOrderByIdDesc();

}