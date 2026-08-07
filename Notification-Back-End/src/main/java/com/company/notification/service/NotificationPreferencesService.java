package com.company.notification.service;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.dto.NotificationPreferencesUpdateRequest;

public interface NotificationPreferencesService {

    NotificationPreferencesResponse getPreferencesForUser(Long userId);

    NotificationPreferencesResponse replacePreferences(Long userId, NotificationPreferencesUpdateRequest request);

    NotificationPreferencesResponse patchPreferences(Long userId, NotificationPreferencesPatchRequest request);
}
