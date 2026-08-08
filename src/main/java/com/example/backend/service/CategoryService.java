package com.example.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.backend.entity.Category;
import com.example.backend.repository.CategoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }
    // ==========================================
// Pagination
// ==========================================

public Page<Category> getCategories(int page, int size) {

    Pageable pageable = PageRequest.of(page, size);

    return categoryRepository.findAll(pageable);
}
    // ==========================
    // Create Category
    // ==========================

    public Category createCategory(Category category) {

        if (categoryRepository.existsByCategoryName(category.getCategoryName())) {
            throw new RuntimeException("Category already exists.");
        }

        if (category.getStatus() == null || category.getStatus().isBlank()) {
            category.setStatus("ACTIVE");
        }

        return categoryRepository.save(category);
    }

    // ==========================
    // Get All Categories
    // ==========================
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    // ==========================
    // Get Category By ID
    // ==========================
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    // ==========================
    // Get Category By Name
    // ==========================
    public Optional<Category> getCategoryByName(String categoryName) {
        return categoryRepository.findByCategoryName(categoryName);
    }

    // ==========================
    // Update Category
    // ==========================
   public Category updateCategory(Long id, Category category) {

    Category existingCategory = categoryRepository.findById(id)
            .orElseThrow(() ->
                    new RuntimeException("Category not found"));

    // Check duplicate name only if the name is changed
    if (!existingCategory.getCategoryName()
            .equalsIgnoreCase(category.getCategoryName())
            && categoryRepository.existsByCategoryName(category.getCategoryName())) {

        throw new RuntimeException("Category already exists.");
    }

    existingCategory.setCategoryName(category.getCategoryName());
    existingCategory.setDescription(category.getDescription());
    existingCategory.setRetentionPeriod(category.getRetentionPeriod());
    existingCategory.setStatus(category.getStatus());

    return categoryRepository.save(existingCategory);
}

    // ==========================
    // Delete Category
    // ==========================
    public void deleteCategory(Long id) {
        categoryRepository.deleteById(id);
    }

}