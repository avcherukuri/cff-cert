package com.company.notification.security;

/** Minimal test double for whatever principal type the real (out-of-scope) auth module provides. */
public record TestUserPrincipal(Long userId) implements AuthenticatedUserPrincipal {

    @Override
    public Long getUserId() {
        return userId;
    }
}
