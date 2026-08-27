package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.ApprovalHistory;
import com.example.backend.entity.Document;

@Repository
public interface ApprovalHistoryRepository
        extends JpaRepository<ApprovalHistory, Long> {

    // Get workflow history of a document
    List<ApprovalHistory> findByDocumentIdOrderByActionDateDesc(
            Long documentId);

    // Get workflow history performed by a user
    List<ApprovalHistory> findByActionBy(String actionBy);

    // Used by ApprovalHistoryService
    @Modifying
    @Query("DELETE FROM ApprovalHistory h WHERE h.document.id = :documentId")
    void deleteByDocumentId(@Param("documentId") Long documentId);

    // Used by DocumentService permanentDelete()
    void deleteByDocument(Document document);
}