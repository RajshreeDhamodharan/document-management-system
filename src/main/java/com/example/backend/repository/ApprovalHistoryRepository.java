package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.ApprovalHistory;

@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {

    // Get workflow history of a document
    List<ApprovalHistory> findByDocumentIdOrderByActionDateDesc(Long documentId);

    // Get workflow history performed by a user
    List<ApprovalHistory> findByActionBy(String actionBy);

}