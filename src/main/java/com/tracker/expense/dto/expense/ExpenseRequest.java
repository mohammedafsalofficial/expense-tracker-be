package com.tracker.expense.dto.expense;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ExpenseRequest {

    @Min(value = 1, message = "Amount must be greater than zero")
    private double amount;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    private String description;
}
