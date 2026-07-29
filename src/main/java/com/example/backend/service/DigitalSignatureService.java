package com.example.backend.service;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.LocalDate;
import java.util.Base64;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.backend.dto.DigitalSignatureResponseDTO;
import com.example.backend.entity.DigitalSignature;
import com.example.backend.entity.Document;
import com.example.backend.repository.DigitalSignatureRepository;
import com.example.backend.repository.DocumentRepository;

@Service
public class DigitalSignatureService {

    private final DigitalSignatureRepository signatureRepository;
    private final DocumentRepository documentRepository;

    private static final String PRIVATE_KEY_FILE = "private.key";
    private static final String PUBLIC_KEY_FILE = "public.key";

    public DigitalSignatureService(
            DigitalSignatureRepository signatureRepository,
            DocumentRepository documentRepository) {

        this.signatureRepository = signatureRepository;
        this.documentRepository = documentRepository;
    }

    // ==========================================
    // Generate RSA Key Pair (only once)
    // ==========================================

    private void generateKeys() throws Exception {

        if (Files.exists(Paths.get(PRIVATE_KEY_FILE))
                && Files.exists(Paths.get(PUBLIC_KEY_FILE))) {
            return;
        }

        KeyPairGenerator generator =
                KeyPairGenerator.getInstance("RSA");

        generator.initialize(2048);

        KeyPair pair = generator.generateKeyPair();

        Files.write(
                Paths.get(PRIVATE_KEY_FILE),
                pair.getPrivate().getEncoded());

        Files.write(
                Paths.get(PUBLIC_KEY_FILE),
                pair.getPublic().getEncoded());
    }
        // ==========================================
    // Load Private Key
    // ==========================================

    private PrivateKey getPrivateKey() throws Exception {

        byte[] keyBytes =
                Files.readAllBytes(Paths.get(PRIVATE_KEY_FILE));

        PKCS8EncodedKeySpec spec =
                new PKCS8EncodedKeySpec(keyBytes);

        KeyFactory factory =
                KeyFactory.getInstance("RSA");

        return factory.generatePrivate(spec);
    }

    // ==========================================
    // Load Public Key
    // ==========================================

    private PublicKey getPublicKey() throws Exception {

        byte[] keyBytes =
                Files.readAllBytes(Paths.get(PUBLIC_KEY_FILE));

        X509EncodedKeySpec spec =
                new X509EncodedKeySpec(keyBytes);

        KeyFactory factory =
                KeyFactory.getInstance("RSA");

        return factory.generatePublic(spec);
    }

    // ==========================================
    // Generate SHA-256 Hash
    // ==========================================

    private String generateHash(Document document)
            throws Exception {

        String content =
                document.getTitle()
                + document.getDescription()
                + document.getCategory()
                + document.getFileName()
                + document.getFilePath()
                + document.getUploadedBy()
                + document.getUploadDate();

        MessageDigest digest =
                MessageDigest.getInstance("SHA-256");

        byte[] hash =
                digest.digest(
                        content.getBytes(StandardCharsets.UTF_8));

        return Base64.getEncoder()
                .encodeToString(hash);
    }

    // ==========================================
    // Sign Document
    // ==========================================

    public void signDocument(Long documentId)
            throws Exception {

        generateKeys();

        Document document =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Document not found"));

        if (signatureRepository.existsByDocument(document)) {
            return;
        }

        String hash = generateHash(document);

        Signature signature =
                Signature.getInstance("SHA256withRSA");

        signature.initSign(getPrivateKey());

        signature.update(
                hash.getBytes(StandardCharsets.UTF_8));

        String digitalSignature =
                Base64.getEncoder()
                        .encodeToString(signature.sign());

        Authentication authentication =
                SecurityContextHolder.getContext()
                        .getAuthentication();

        DigitalSignature entity =
                new DigitalSignature();

        entity.setDocument(document);
        entity.setHash(hash);
        entity.setSignature(digitalSignature);
        entity.setAlgorithm("SHA256withRSA");
        entity.setSignedBy(authentication.getName());
        entity.setSignedDate(LocalDate.now().toString());

        signatureRepository.save(entity);
    }
    // ==========================================
    // Verify Signature
    // ==========================================

    public boolean verifySignature(Long documentId)
            throws Exception {

        Document document = documentRepository.findById(documentId)
                .orElseThrow(() ->
                        new RuntimeException("Document not found"));

        DigitalSignature digitalSignature =
                signatureRepository.findByDocument(document)
                        .orElseThrow(() ->
                                new RuntimeException("Digital signature not found"));

        String currentHash = generateHash(document);

        Signature signature =
                Signature.getInstance("SHA256withRSA");

        signature.initVerify(getPublicKey());

        signature.update(
                currentHash.getBytes(StandardCharsets.UTF_8));

        byte[] signatureBytes =
                Base64.getDecoder()
                        .decode(digitalSignature.getSignature());

        return signature.verify(signatureBytes);
    }

    // ==========================================
    // Get Signature Details
    // ==========================================

    public DigitalSignatureResponseDTO getSignature(Long documentId)
            throws Exception {

        Document document =
                documentRepository.findById(documentId)
                        .orElseThrow(() ->
                                new RuntimeException("Document not found"));

        DigitalSignature signature =
                signatureRepository.findByDocument(document)
                        .orElseThrow(() ->
                                new RuntimeException("Digital signature not found"));

        DigitalSignatureResponseDTO dto =
                convert(signature);

        dto.setValid(verifySignature(documentId));

        return dto;
    }

    // ==========================================
    // Entity → DTO
    // ==========================================

    private DigitalSignatureResponseDTO convert(
            DigitalSignature signature) {

        DigitalSignatureResponseDTO dto =
                new DigitalSignatureResponseDTO();

        dto.setId(signature.getId());
        dto.setDocumentId(signature.getDocument().getId());
        dto.setHash(signature.getHash());
        dto.setAlgorithm(signature.getAlgorithm());
        dto.setSignedBy(signature.getSignedBy());
        dto.setSignedDate(signature.getSignedDate());

        return dto;
    }

}