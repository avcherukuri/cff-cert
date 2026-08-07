package com.company.notification.dto;

import java.time.Instant;

public record NotificationPreferencesResponse(
        boolean emailEnabled,
        boolean smsEnabled,
        boolean pushEnabled,
        Instant updatedAt
) {
}
