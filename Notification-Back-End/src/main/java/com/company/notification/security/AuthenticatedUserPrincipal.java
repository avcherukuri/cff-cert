package com.company.notification.security;

/**
 * Integration point with the platform's (out-of-scope) session-auth module.
 * Whatever principal type that module places in the SecurityContext must
 * implement this so {@link CurrentUserProvider} can resolve the caller's id.
 */
public interface AuthenticatedUserPrincipal {

    Long getUserId();
}
