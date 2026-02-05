package com.tracker.expense.dto.category;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CategoryRequest {

    @NotBlank(message = "Category must be provided")
    private String category;
}
