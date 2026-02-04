package com.tracker.expense.dto.expense;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class ExpenseInsightResponse {

    private String month;
    private Double totalSpent;
    private Double previousMonthSpent;
    private Double changePercent;
    private String trend;
    private List<ExpenseCategorySummary> topCategories;
    private String advice;
}
