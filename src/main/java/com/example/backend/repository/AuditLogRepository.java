package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {

    // Get logs by user email
    List<AuditLog> findByUserEmailOrderByCreatedAtDesc(String userEmail);

    // Get logs by action
    List<AuditLog> findByActionOrderByCreatedAtDesc(String action);

    // Get logs by module
    List<AuditLog> findByModuleOrderByCreatedAtDesc(String module);

    // ===============================
    // Dashboard Methods
    // ===============================

    List<AuditLog> findTop5ByOrderByIdDesc();

}