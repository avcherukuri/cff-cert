package com.company.notification.dto;

import com.company.notification.validation.AtLeastOneFieldPresent;

/** Partial-update (PATCH) payload — unset fields are left unchanged. */
@AtLeastOneFieldPresent(message = "at least one of emailEnabled, smsEnabled, pushEnabled must be provided")
public record NotificationPreferencesPatchRequest(
        Boolean emailEnabled,
        Boolean smsEnabled,
        Boolean pushEnabled
) {
}
