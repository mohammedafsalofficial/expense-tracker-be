package com.tracker.expense.repository.category;

import com.tracker.expense.model.auth.User;
import com.tracker.expense.model.category.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    @Query("""
        SELECT COUNT(c) > 0 FROM Category c
        WHERE c.user = :user
        AND c.category = :category
    """)
    boolean existsCategoryForUser(User user, String category);
}
