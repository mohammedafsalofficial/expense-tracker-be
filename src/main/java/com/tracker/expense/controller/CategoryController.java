package com.tracker.expense.controller;

import com.tracker.expense.dto.ApiResponse;
import com.tracker.expense.dto.category.CategoryRequest;
import com.tracker.expense.dto.category.CategoryResponse;
import com.tracker.expense.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<ApiResponse<CategoryResponse>> createCategory(@Valid @RequestBody CategoryRequest dto) {
        CategoryResponse response = categoryService.createCategory(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.of("Category created successfully", response));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<CategoryResponse>>> getAllCategories() {
        List<CategoryResponse> response = categoryService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.of("Categories fetched successfully", response));
    }
}
