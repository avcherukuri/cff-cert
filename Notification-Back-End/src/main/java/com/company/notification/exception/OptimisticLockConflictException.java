package com.company.notification.exception;

public class OptimisticLockConflictException extends RuntimeException {

    public OptimisticLockConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
