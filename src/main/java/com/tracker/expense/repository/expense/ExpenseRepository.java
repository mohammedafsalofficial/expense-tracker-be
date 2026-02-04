package com.tracker.expense.repository.expense;

import com.tracker.expense.model.auth.User;
import com.tracker.expense.model.expense.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long>, JpaSpecificationExecutor<Expense> {

    @Query("""
        SELECT SUM(e.amount) FROM Expense e
        WHERE e.user = :user AND e.date BETWEEN :start AND :end
    """)
    Double getTotalSpent(User user, LocalDateTime start, LocalDateTime end);

    @Query("""
        SELECT e.category, SUM(e.amount) FROM Expense e
        WHERE e.user = :user AND e.date BETWEEN :start AND :end
        GROUP BY e.category
        ORDER BY SUM(e.amount) DESC
    """)
    List<Object[]> getCategoryWiseTotal(User user, LocalDateTime start, LocalDateTime end);
}
