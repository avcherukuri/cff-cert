package com.company.notification.exception;

import com.company.notification.dto.FieldErrorDetail;
import com.company.notification.dto.ValidationErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        List<FieldErrorDetail> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDetail(error.getField(), error.getDefaultMessage()))
                .toList();

        // Class-level constraint violations (e.g. @AtLeastOneFieldPresent) have no field name.
        List<FieldErrorDetail> globalErrors = ex.getBindingResult().getGlobalErrors().stream()
                .map(error -> new FieldErrorDetail(error.getObjectName(), error.getDefaultMessage()))
                .toList();

        List<FieldErrorDetail> allErrors = fieldErrors.isEmpty() ? globalErrors : fieldErrors;

        ValidationErrorResponse body = ValidationErrorResponse.of(
                HttpStatus.BAD_REQUEST.value(), "VALIDATION_ERROR", "Request validation failed", allErrors);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(OptimisticLockConflictException.class)
    public ResponseEntity<ValidationErrorResponse> handleOptimisticLockConflict(OptimisticLockConflictException ex) {
        ValidationErrorResponse body = ValidationErrorResponse.of(
                HttpStatus.CONFLICT.value(), "CONFLICT", ex.getMessage(), List.of());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ValidationErrorResponse> handleUnexpected(Exception ex) {
        log.error("Unexpected error handling notification-preferences request", ex);
        ValidationErrorResponse body = ValidationErrorResponse.of(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "INTERNAL_ERROR", "An unexpected error occurred", List.of());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
