package com.tracker.expense.service;

import com.tracker.expense.dto.expense.ExpenseRequest;
import com.tracker.expense.dto.expense.ExpenseResponse;
import com.tracker.expense.model.auth.User;
import com.tracker.expense.model.expense.Expense;
import com.tracker.expense.repository.expense.ExpenseRepository;
import com.tracker.expense.security.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    public List<ExpenseResponse> getExpenses(
            String category,
            String startDate,
            String endDate,
            Double minAmount,
            Double maxAmount) {
        String email = securityUtil.getCurrentUserEmail();
        User user = userService.getUserByEmail(email);

        String normalizedCategory = (category != null && !category.isBlank())
                ? category.trim().toLowerCase()
                : null;
        LocalDateTime start = startDate != null ? LocalDate.parse(startDate).atStartOfDay() : null;
        LocalDateTime end = endDate != null ? LocalDate.parse(endDate).atTime(LocalTime.MAX) : null;

        Specification<Expense> spec = Specification.where(userEquals(user));

        Specification<Expense> categorySpec = categoryEquals(normalizedCategory);
        if (categorySpec != null) spec = spec.and(categorySpec);

        Specification<Expense> dateSpec = dateBetween(start, end);
        if (dateSpec != null) spec = spec.and(dateSpec);

        Specification<Expense> amountSpec = amountBetween(minAmount, maxAmount);
        if (amountSpec != null) spec = spec.and(amountSpec);

        List<Expense> expenses = expenseRepository.findAll(spec);
        return expenses.stream().map(this::mapToResponse).toList();
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

    private Specification<Expense> userEquals(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    private Specification<Expense> categoryEquals(String category) {
        if (category == null) return null;
        return (root, query, cb) -> cb.equal(cb.lower(root.get("category")), category);
    }

    private Specification<Expense> dateBetween(LocalDateTime start, LocalDateTime end) {
        if (start == null && end == null) return null;
        if (start != null && end != null)
            return (root, query, cb) -> cb.between(root.get("date"), start, end);
        if (start != null)
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), start);
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), end);
    }

    private Specification<Expense> amountBetween(Double min, Double max) {
        if (min == null && max == null) return null;
        if (min != null && max != null)
            return (root, query, cb) -> cb.between(root.get("amount"), min, max);
        if (min != null)
            return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("amount"), min);
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("amount"), max);
    }
}
