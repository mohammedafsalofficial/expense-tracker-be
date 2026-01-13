package com.tracker.expense.dto;

import lombok.Builder;

@Builder
public record UserResponse(
        Long id,
        String name,
        String email
) {
}
