package com.example.backend.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.backend.entity.Category;
import com.example.backend.service.CategoryService;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    private final CategoryService categoryService;
    @GetMapping("/page")
public ResponseEntity<Page<Category>> getCategories(

        @RequestParam(defaultValue = "0") int page,

        @RequestParam(defaultValue = "10") int size) {

    return ResponseEntity.ok(
            categoryService.getCategories(page, size));
}

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // ==========================
    // Create Category
    // ==========================
    @PostMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
    public Category createCategory(@RequestBody Category category) {
        return categoryService.createCategory(category);
    }

    // ==========================
    // Get All Categories
    // ==========================
    @GetMapping
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public List<Category> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // ==========================
    // Get Category By ID
    // ==========================
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public Optional<Category> getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    // ==========================
    // Get Category By Name
    // ==========================
    @GetMapping("/name/{categoryName}")
    @PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER','EDITOR','VIEWER','APPROVER')")
    public Optional<Category> getCategoryByName(@PathVariable String categoryName) {
        return categoryService.getCategoryByName(categoryName);
    }

    // ==========================
    // Update Category
    // ==========================
   @PutMapping("/{id}")
@PreAuthorize("hasAnyRole('SUPER_ADMIN','OWNER')")
public Category updateCategory(
        @PathVariable Long id,
        @RequestBody Category category) {

    return categoryService.updateCategory(id, category);
}
    // ==========================
    // Delete Category
    // ==========================
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public String deleteCategory(@PathVariable Long id) {

        categoryService.deleteCategory(id);

        return "Category deleted successfully!";
    }

}