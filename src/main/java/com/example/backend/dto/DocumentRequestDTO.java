package com.example.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class DocumentRequestDTO {

    @NotBlank(message = "Title cannot be empty")
    private String title;

    @NotBlank(message = "Description cannot be empty")
    private String description;

    @NotBlank(message = "Category cannot be empty")
    private String category;
   private String retentionDate;

    // Default Constructor
    public DocumentRequestDTO() {
    }

    // Parameterized Constructor
    public DocumentRequestDTO(String title,
                              String description,
                              String category) {

        this.title = title;
        this.description = description;
        this.category = category;
    }

    // ======================
    // Getters and Setters
    // ======================

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
   public String getRetentionDate() {
    return retentionDate;
}

public void setRetentionDate(String retentionDate) {
    this.retentionDate = retentionDate;
}
}