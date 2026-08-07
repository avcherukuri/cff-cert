package com.company.notification.security;

/** Dev-profile-only stand-in for the platform's (out-of-scope) session-auth module's principal. */
public record DevUserPrincipal(Long userId) implements AuthenticatedUserPrincipal {

    @Override
    public Long getUserId() {
        return userId;
    }
}
