package com.example.backend.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "digital_signatures")
public class DigitalSignature {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // One signature for one document
    @OneToOne
    @JoinColumn(name = "document_id", nullable = false)
    private Document document;

    @Column(length = 500)
    private String hash;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String signature;

    private String algorithm;

    private String signedBy;

    private String signedDate;

    // ==========================
    // Default Constructor
    // ==========================

    public DigitalSignature() {
    }

    // ==========================
    // Parameterized Constructor
    // ==========================

    public DigitalSignature(Long id,
                            Document document,
                            String hash,
                            String signature,
                            String algorithm,
                            String signedBy,
                            String signedDate) {

        this.id = id;
        this.document = document;
        this.hash = hash;
        this.signature = signature;
        this.algorithm = algorithm;
        this.signedBy = signedBy;
        this.signedDate = signedDate;
    }

    // ==========================
    // Getters & Setters
    // ==========================

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Document getDocument() {
        return document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getAlgorithm() {
        return algorithm;
    }

    public void setAlgorithm(String algorithm) {
        this.algorithm = algorithm;
    }

    public String getSignedBy() {
        return signedBy;
    }

    public void setSignedBy(String signedBy) {
        this.signedBy = signedBy;
    }

    public String getSignedDate() {
        return signedDate;
    }

    public void setSignedDate(String signedDate) {
        this.signedDate = signedDate;
    }
}