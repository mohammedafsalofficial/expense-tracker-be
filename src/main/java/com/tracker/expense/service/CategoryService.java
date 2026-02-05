package com.tracker.expense.service;

import com.tracker.expense.dto.category.CategoryRequest;
import com.tracker.expense.dto.category.CategoryResponse;
import com.tracker.expense.model.auth.User;
import com.tracker.expense.model.category.Category;
import com.tracker.expense.repository.category.CategoryRepository;
import com.tracker.expense.security.SecurityUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final SecurityUtil securityUtil;
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(@Valid CategoryRequest dto) {
        User user = securityUtil.getCurrentUser();

        String normalizedCategory = dto.getCategory().trim().toLowerCase();
        boolean exists = categoryRepository.existsCategoryForUser(user, normalizedCategory);
        if (exists)
            throw new IllegalArgumentException(String.format("Category %s already exists for this user", normalizedCategory));

        Category category = Category.builder()
                .category(dto.getCategory())
                .user(user)
                .build();
        Category savedCategory = categoryRepository.save(category);
        return CategoryResponse.builder()
                .id(savedCategory.getId())
                .category(savedCategory.getCategory())
                .build();
    }

    public List<CategoryResponse> getAllCategories() {
        User user = securityUtil.getCurrentUser();
        List<Category> categories = categoryRepository.findAllByUser(user);
        return categories.stream().map(this::mapToResponse).toList();
    }

    private CategoryResponse mapToResponse(Category category) {
        return CategoryResponse.builder()
                .id(category.getId())
                .category(category.getCategory())
                .build();
    }
}
