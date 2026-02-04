package com.tracker.expense.dto.expense;

import lombok.Getter;

@Getter
public class ExpenseUpdateRequest {

    private Double amount;
    private String category;
    private String description;
}
