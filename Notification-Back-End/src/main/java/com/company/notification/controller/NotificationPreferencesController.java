package com.company.notification.controller;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.dto.NotificationPreferencesUpdateRequest;
import com.company.notification.security.CurrentUserProvider;
import com.company.notification.service.NotificationPreferencesService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Every method resolves the caller's id from the session via
 * {@link CurrentUserProvider} — no method ever accepts a user id from the
 * request path, query string, or body, which removes the IDOR attack surface
 * by construction.
 */
@RestController
@RequestMapping("/api/v1/notification-preferences")
public class NotificationPreferencesController {

    private final NotificationPreferencesService service;
    private final CurrentUserProvider currentUserProvider;

    public NotificationPreferencesController(NotificationPreferencesService service, CurrentUserProvider currentUserProvider) {
        this.service = service;
        this.currentUserProvider = currentUserProvider;
    }

    @GetMapping
    public NotificationPreferencesResponse getMyPreferences() {
        return service.getPreferencesForUser(currentUserProvider.getCurrentUserId());
    }

    @PutMapping
    public NotificationPreferencesResponse replacePreferences(@Valid @RequestBody NotificationPreferencesUpdateRequest request) {
        return service.replacePreferences(currentUserProvider.getCurrentUserId(), request);
    }

    @PatchMapping
    public NotificationPreferencesResponse patchPreferences(@Valid @RequestBody NotificationPreferencesPatchRequest request) {
        return service.patchPreferences(currentUserProvider.getCurrentUserId(), request);
    }
}
