package com.curio.blog.controller;

import com.curio.blog.dto.*;
import com.curio.blog.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryDto>> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryDto category = categoryService.createCategory(request);

        ApiResponse<CategoryDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Category created successfully",
                category,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> updateCategory(@PathVariable Long id,
                                                                   @Valid @RequestBody CategoryRequest request) {
        CategoryDto category = categoryService.updateCategory(id, request);

        ApiResponse<CategoryDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Category updated successfully",
                category,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<CategoryDto>>> getAllCategories() {
        List<CategoryDto> categories = categoryService.getAllCategories();

        ApiResponse<List<CategoryDto>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Categories updated successfully",
                categories,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> getCategoryById(@PathVariable Long id) {
        CategoryDto categories = categoryService.getCategoryById(id);

        ApiResponse<CategoryDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Categories retrieved successfully",
                categories,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<CategoryDto>> deleteCategoryById(@PathVariable Long id) {
        categoryService.deleteCategoryById(id);

        ApiResponse<CategoryDto> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Category deleted successfully",
                null,
                LocalDateTime.now()
        );

        return ResponseEntity.ok(response);
    }
}
