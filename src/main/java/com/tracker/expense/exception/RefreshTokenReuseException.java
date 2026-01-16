package com.tracker.expense.exception;

public class RefreshTokenReuseException extends RuntimeException {

    public RefreshTokenReuseException() {
        super("Refresh token reuse detected. Please login again.");
    }
}
