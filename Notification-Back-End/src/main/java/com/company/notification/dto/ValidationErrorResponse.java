package com.company.notification.dto;

import java.time.Instant;
import java.util.List;

public record ValidationErrorResponse(
        int status,
        String error,
        String message,
        List<FieldErrorDetail> fieldErrors,
        Instant timestamp
) {
    public static ValidationErrorResponse of(int status, String error, String message, List<FieldErrorDetail> fieldErrors) {
        return new ValidationErrorResponse(status, error, message, fieldErrors, Instant.now());
    }
}
