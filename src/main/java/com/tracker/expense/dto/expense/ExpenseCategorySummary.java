package com.tracker.expense.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExpenseCategorySummary {

    private String category;
    private Double amount;
}
