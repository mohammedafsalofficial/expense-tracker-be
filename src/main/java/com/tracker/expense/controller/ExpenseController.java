package com.tracker.expense.controller;

import com.tracker.expense.dto.ApiResponse;
import com.tracker.expense.dto.expense.ExpenseInsightResponse;
import com.tracker.expense.dto.expense.ExpenseRequest;
import com.tracker.expense.dto.expense.ExpenseResponse;
import com.tracker.expense.dto.expense.ExpenseUpdateRequest;
import com.tracker.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ExpenseResponse>>> getExpenses(
            @RequestParam(required = false) String category,
            @RequestParam(required = false, name = "start_date") String startDate,
            @RequestParam(required = false, name = "end_date") String endDate,
            @RequestParam(required = false, name = "min_amount") Double minAmount,
            @RequestParam(required = false, name = "max_amount") Double maxAmount,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "date", name = "sort_by") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Page<ExpenseResponse> response = expenseService.getExpenses(
                category, startDate, endDate, minAmount, maxAmount, page, size, sortBy, direction
        );
        return ResponseEntity.ok(ApiResponse.of("Expenses fetched successfully.", response));
    }

    @GetMapping("/insights")
    public ResponseEntity<ApiResponse<ExpenseInsightResponse>> getInsights(@RequestParam(required = false) String month) {
        ExpenseInsightResponse insights = expenseService.getInsights(month);
        return ResponseEntity.ok(ApiResponse.of("Insights fetched successfully", insights));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> addNewExpense(@Valid @RequestBody ExpenseRequest requestDTO) {
        ExpenseResponse response = expenseService.addNewExpense(requestDTO);
        return ResponseEntity.ok(ApiResponse.of("Expense created successfully.", response));
    }

    @PatchMapping("{id}")
    public ResponseEntity<ApiResponse<ExpenseResponse>> updateExpense(
            @PathVariable Long id, @RequestBody ExpenseUpdateRequest requestDTO) {
        ExpenseResponse expenseResponse = expenseService.updateExpense(id, requestDTO);
        return ResponseEntity.ok(ApiResponse.of("Expense updated successfully", expenseResponse));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
