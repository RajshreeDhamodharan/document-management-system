package com.example.backend.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.example.backend.service.OCRService;

@RestController
@RequestMapping("/api/ocr")
@CrossOrigin(origins = "http://localhost:3000")
public class OCRController {

    private final OCRService ocrService;

    public OCRController(OCRService ocrService) {
        this.ocrService = ocrService;
    }

    @PostMapping("/extract")
    public ResponseEntity<String> extractText(
            @RequestParam("file") MultipartFile file) {

        try {

            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                        .body("Please select a file.");
            }

            // Create temporary file
            Path tempFile = Files.createTempFile(
                    "ocr_",
                    "_" + file.getOriginalFilename()
            );

            file.transferTo(tempFile.toFile());

            // Extract text
            String extractedText =
                    ocrService.extractText(tempFile.toString());

            // Delete temporary file
            Files.deleteIfExists(tempFile);

            return ResponseEntity.ok(extractedText);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity.internalServerError()
                    .body("OCR extraction failed: " + e.getMessage());
        }
    }
}