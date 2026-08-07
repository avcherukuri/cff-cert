package com.company.notification.dto;

import jakarta.validation.constraints.NotNull;

/** Full-replace (PUT) payload — every channel must be explicitly specified. */
public record NotificationPreferencesUpdateRequest(
        @NotNull(message = "must not be null") Boolean emailEnabled,
        @NotNull(message = "must not be null") Boolean smsEnabled,
        @NotNull(message = "must not be null") Boolean pushEnabled
) {
}
