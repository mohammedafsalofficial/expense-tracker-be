package com.tracker.expense.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Map;

@Builder
public record ApiErrorResponse(
        boolean success,
        String message,
        Map<String, String> errors,
        LocalDateTime timestamp) {
    public static ApiErrorResponse of(String message, Map<String, String> errors) {
        return ApiErrorResponse.builder()
                .success(false)
                .message(message)
                .errors(errors == null ? Map.of() : errors)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
