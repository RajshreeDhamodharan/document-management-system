package com.example.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.backend.entity.Document;
import com.example.backend.entity.DocumentStatus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface DocumentRepository extends JpaRepository<Document, Long>,
        JpaSpecificationExecutor<Document> {

    // ==========================================
    // Basic Search Methods
    // ==========================================

    List<Document> findByTitleContainingIgnoreCase(String title);

    List<Document> findByCategory(String category);

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByUploadedBy(String uploadedBy);
    
List<Document> findByArchivedTrue();

List<Document> findByArchivedFalse();

            List<Document> findByArchivedFalseAndRetentionDateIsNotNull();
long countByStatus(DocumentStatus status);



long countByArchivedTrue();

long countByArchivedFalse();
            // ==========================================
// Full Text Search (OCR Content)
// ==========================================
            
@Query("""
    SELECT d
    FROM Document d
    WHERE d.archived = false
      AND LOWER(d.extractedText) LIKE LOWER(CONCAT('%', :keyword, '%'))
""")
List<Document> searchByExtractedText(@Param("keyword") String keyword);
 List<Document> findTop5ByOrderByIdDesc();

@Query("""
SELECT d.category, COUNT(d)
FROM Document d
GROUP BY d.category
""")
List<Object[]> countDocumentsByCategory();

@Query("""
SELECT SUBSTRING(d.uploadDate,1,7), COUNT(d)
FROM Document d
GROUP BY SUBSTRING(d.uploadDate,1,7)
ORDER BY SUBSTRING(d.uploadDate,1,7)
""")
List<Object[]> countMonthlyUploads();


}