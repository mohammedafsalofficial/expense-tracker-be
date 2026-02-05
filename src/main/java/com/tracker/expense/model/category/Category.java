package com.tracker.expense.model.category;

import com.tracker.expense.model.auth.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"category", "user_id"})
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    @PreUpdate
    public void normalizeCategory() {
        if (category != null) category = category.trim().toLowerCase();
    }
}
