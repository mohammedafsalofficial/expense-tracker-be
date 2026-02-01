package com.tracker.expense.dto.expense;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class ExpenseResponse {

    private Long id;
    private double amount;
    private String category;
    private String description;
    private LocalDateTime date;
}
