package com.tracker.expense.dto.auth;

import com.tracker.expense.enums.Role;
import lombok.Builder;

@Builder
public record AuthResponse(
        Long id,
        String name,
        String email,
        Role role
) {
}
