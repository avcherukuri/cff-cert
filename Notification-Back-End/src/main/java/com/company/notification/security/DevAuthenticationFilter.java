package com.company.notification.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Dev-profile-only stand-in for the platform's (out-of-scope) session-auth module.
 * Authenticates every request as a fixed dev user so the API can be exercised
 * locally (e.g. via Postman or the frontend) before that module exists.
 * Only ever wired in when the "dev" profile is active — see SecurityConfig.
 */
public class DevAuthenticationFilter extends OncePerRequestFilter {

    static final long DEV_USER_ID = 1L;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        var principal = new DevUserPrincipal(DEV_USER_ID);
        var authentication = new UsernamePasswordAuthenticationToken(principal, null, List.of());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
