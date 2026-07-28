package com.example.backend.specification;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.domain.Specification;

import com.example.backend.dto.DocumentSearchDTO;
import com.example.backend.entity.Document;

import jakarta.persistence.criteria.Predicate;

public class DocumentSpecification {

    public static Specification<Document> search(DocumentSearchDTO searchDTO) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            // ==========================================
            // Title Search
            // ==========================================

            if (searchDTO.getTitle() != null &&
                    !searchDTO.getTitle().isBlank()) {

                predicates.add(

                        criteriaBuilder.like(

                                criteriaBuilder.lower(root.get("title")),

                                "%" + searchDTO.getTitle().toLowerCase() + "%"
                        )

                );
            }

            // ==========================================
            // Category Search
            // ==========================================

            if (searchDTO.getCategory() != null &&
                    !searchDTO.getCategory().isBlank()) {

                predicates.add(

                        criteriaBuilder.equal(
                                root.get("category"),
                                searchDTO.getCategory()
                        )

                );
            }

            // ==========================================
            // Status Search
            // ==========================================

            if (searchDTO.getStatus() != null) {

                predicates.add(

                        criteriaBuilder.equal(
                                root.get("status"),
                                searchDTO.getStatus()
                        )

                );
            }

            // ==========================================
            // Uploaded By Search
            // ==========================================

            if (searchDTO.getUploadedBy() != null &&
                    !searchDTO.getUploadedBy().isBlank()) {

                predicates.add(

                        criteriaBuilder.like(

                                criteriaBuilder.lower(root.get("uploadedBy")),

                                "%" + searchDTO.getUploadedBy().toLowerCase() + "%"
                        )

                );
            }
            // ==========================================
// From Date
// ==========================================

if (searchDTO.getFromDate() != null &&
        !searchDTO.getFromDate().isBlank()) {

    predicates.add(

            criteriaBuilder.greaterThanOrEqualTo(
                    root.get("uploadDate"),
                    searchDTO.getFromDate()
            )

    );
}

// ==========================================
// To Date
// ==========================================

if (searchDTO.getToDate() != null &&
        !searchDTO.getToDate().isBlank()) {

    predicates.add(

            criteriaBuilder.lessThanOrEqualTo(
                    root.get("uploadDate"),
                    searchDTO.getToDate()
            )

    );
}

            // ==========================================
            // Return Combined Predicates
            // ==========================================

            return criteriaBuilder.and(
                    predicates.toArray(new Predicate[0]));
        };
    }

}