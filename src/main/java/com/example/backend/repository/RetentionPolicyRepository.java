package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.RetentionPolicy;

@Repository
public interface RetentionPolicyRepository extends JpaRepository<RetentionPolicy, Long> {

    Optional<RetentionPolicy> findByCategory(String category);
}