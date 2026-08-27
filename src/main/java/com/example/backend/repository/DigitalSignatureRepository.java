package com.example.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.DigitalSignature;
import com.example.backend.entity.Document;

@Repository
public interface DigitalSignatureRepository
        extends JpaRepository<DigitalSignature, Long> {

    Optional<DigitalSignature> findByDocument(Document document);

    boolean existsByDocument(Document document);

    // Delete digital signature for a document
    void deleteByDocument(Document document);
}