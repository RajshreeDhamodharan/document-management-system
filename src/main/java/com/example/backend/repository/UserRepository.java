package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Role;
import com.example.backend.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // ==========================================
    // Authentication
    // ==========================================

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    // ==========================================
    // Dashboard Statistics
    // ==========================================

    long countByRole(Role role);

    // ==========================================
    // Profile Module
    // ==========================================

    Optional<User> findById(Long id);

}