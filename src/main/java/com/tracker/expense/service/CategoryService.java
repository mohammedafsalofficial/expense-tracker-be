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

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final SecurityUtil securityUtil;
    private final CategoryRepository categoryRepository;

    public CategoryResponse createCategory(@Valid CategoryRequest dto) {
        User user = securityUtil.getCurrentUser();
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
}
