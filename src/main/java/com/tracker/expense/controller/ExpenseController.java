package com.tracker.expense.controller;

import com.tracker.expense.dto.ApiResponse;
import com.tracker.expense.dto.expense.ExpenseRequest;
import com.tracker.expense.dto.expense.ExpenseResponse;
import com.tracker.expense.service.ExpenseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<List<ExpenseResponse>>> getAllExpenses() {
        List<ExpenseResponse> response = expenseService.getAllExpenses();
        return ResponseEntity.ok(ApiResponse.of("Expenses fetched successfully.", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<ExpenseResponse>> addNewExpense(@Valid @RequestBody ExpenseRequest expenseRequest) {
        ExpenseResponse response = expenseService.addNewExpense(expenseRequest);
        return ResponseEntity.ok(ApiResponse.of("Expense created successfully.", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
