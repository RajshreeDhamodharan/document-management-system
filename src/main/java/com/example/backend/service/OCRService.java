package com.example.backend.service;

import java.io.File;
import java.io.FileInputStream;

import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

@Service
public class OCRService {

    private final Tika tika = new Tika();

    // ==========================================
    // Extract Text
    // ==========================================

    public String extractText(String filePath) {

        try {

            File file = new File(filePath);

            if (!file.exists()) {
                return "";
            }

            String lower = file.getName().toLowerCase();

            // ==========================================
            // Image OCR
            // ==========================================

            if (lower.endsWith(".png")
                    || lower.endsWith(".jpg")
                    || lower.endsWith(".jpeg")) {

                return extractImageText(file);
            }

            // ==========================================
            // PDF / DOCX / TXT
            // ==========================================

            FileInputStream inputStream =
                    new FileInputStream(file);

            String text = tika.parseToString(inputStream);

            inputStream.close();

            return text;

        } catch (Exception e) {

            e.printStackTrace();

            return "";
        }
    }

    // ==========================================
    // Image OCR using Tesseract
    // ==========================================

    private String extractImageText(File file)
            throws TesseractException {

        Tesseract tesseract = new Tesseract();

        tesseract.setDatapath(
                "C:\\Program Files\\Tesseract-OCR\\tessdata");

        tesseract.setLanguage("eng");

        return tesseract.doOCR(file);
    }

}