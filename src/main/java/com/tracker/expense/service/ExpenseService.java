package com.tracker.expense.service;

import com.tracker.expense.dto.expense.ExpenseRequest;
import com.tracker.expense.dto.expense.ExpenseResponse;
import com.tracker.expense.model.auth.User;
import com.tracker.expense.model.expense.Expense;
import com.tracker.expense.repository.expense.ExpenseRepository;
import com.tracker.expense.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ExpenseService {

    private final UserService userService;
    private final ExpenseRepository expenseRepository;
    private final SecurityUtil securityUtil;

    private ExpenseResponse mapToResponse(Expense expense) {
        return ExpenseResponse.builder()
                .id(expense.getId())
                .amount(expense.getAmount())
                .category(expense.getCategory())
                .description(expense.getDescription())
                .date(expense.getDate())
                .build();
    }

    public List<ExpenseResponse> getAllExpenses() {
        String email = securityUtil.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);
        return user.getExpenses().stream().map(this::mapToResponse).toList();
    }

    public ExpenseResponse addNewExpense(ExpenseRequest expenseRequest) {
        String email = securityUtil.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        Expense expense = Expense.builder()
                .amount(expenseRequest.getAmount())
                .category(expenseRequest.getCategory())
                .description(expenseRequest.getDescription())
                .user(user)
                .build();

        Expense savedExpense = expenseRepository.save(expense);

        return ExpenseResponse.builder()
                .id(savedExpense.getId())
                .amount(savedExpense.getAmount())
                .category(savedExpense.getCategory())
                .description(savedExpense.getDescription())
                .date(savedExpense.getDate())
                .build();
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }
}
