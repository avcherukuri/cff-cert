package com.company.notification.controller;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.dto.NotificationPreferencesUpdateRequest;
import com.company.notification.security.CurrentUserProvider;
import com.company.notification.service.NotificationPreferencesService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationPreferencesControllerTest {

    private static final Long USER_ID = 99L;

    @Mock
    private NotificationPreferencesService service;

    @Mock
    private CurrentUserProvider currentUserProvider;

    @Test
    void getMyPreferences_delegatesToServiceWithSessionDerivedUserId() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(USER_ID);
        var expected = new NotificationPreferencesResponse(true, false, true, null);
        when(service.getPreferencesForUser(USER_ID)).thenReturn(expected);

        var controller = new NotificationPreferencesController(service, currentUserProvider);
        NotificationPreferencesResponse actual = controller.getMyPreferences();

        assertThat(actual).isEqualTo(expected);
        verify(service).getPreferencesForUser(USER_ID);
    }

    @Test
    void replacePreferences_delegatesWithSessionDerivedUserId_neverFromRequestBody() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(USER_ID);
        var request = new NotificationPreferencesUpdateRequest(true, true, false);
        var expected = new NotificationPreferencesResponse(true, true, false, null);
        when(service.replacePreferences(USER_ID, request)).thenReturn(expected);

        var controller = new NotificationPreferencesController(service, currentUserProvider);
        NotificationPreferencesResponse actual = controller.replacePreferences(request);

        assertThat(actual).isEqualTo(expected);
        verify(service).replacePreferences(USER_ID, request);
    }

    @Test
    void patchPreferences_delegatesWithSessionDerivedUserId() {
        when(currentUserProvider.getCurrentUserId()).thenReturn(USER_ID);
        var request = new NotificationPreferencesPatchRequest(null, true, null);
        var expected = new NotificationPreferencesResponse(true, true, true, null);
        when(service.patchPreferences(USER_ID, request)).thenReturn(expected);

        var controller = new NotificationPreferencesController(service, currentUserProvider);
        NotificationPreferencesResponse actual = controller.patchPreferences(request);

        assertThat(actual).isEqualTo(expected);
        verify(service).patchPreferences(USER_ID, request);
    }
}
