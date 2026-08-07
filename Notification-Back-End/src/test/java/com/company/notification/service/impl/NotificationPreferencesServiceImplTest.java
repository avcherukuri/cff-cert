package com.company.notification.service.impl;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.dto.NotificationPreferencesUpdateRequest;
import com.company.notification.entity.UserNotificationPreference;
import com.company.notification.exception.OptimisticLockConflictException;
import com.company.notification.mapper.NotificationPreferencesMapper;
import com.company.notification.repository.UserNotificationPreferencesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NotificationPreferencesServiceImplTest {

    private static final Long USER_ID = 7L;

    @Mock
    private UserNotificationPreferencesRepository repository;

    private NotificationPreferencesServiceImpl service;

    private final NotificationPreferencesMapper mapper = new NotificationPreferencesMapper();

    @Captor
    private ArgumentCaptor<UserNotificationPreference> entityCaptor;

    @BeforeEach
    void setUp() {
        service = new NotificationPreferencesServiceImpl(repository, mapper);
    }

    @Test
    void getPreferencesForUser_returnsDefaults_whenNoRowExists() {
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());

        NotificationPreferencesResponse response = service.getPreferencesForUser(USER_ID);

        assertThat(response.emailEnabled()).isTrue();
        assertThat(response.smsEnabled()).isFalse();
        assertThat(response.pushEnabled()).isTrue();
        assertThat(response.updatedAt()).isNull();
    }

    @Test
    void getPreferencesForUser_returnsStoredRow_whenRowExists() {
        UserNotificationPreference existing = new UserNotificationPreference(USER_ID, false, true, false);
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.of(existing));

        NotificationPreferencesResponse response = service.getPreferencesForUser(USER_ID);

        assertThat(response.emailEnabled()).isFalse();
        assertThat(response.smsEnabled()).isTrue();
        assertThat(response.pushEnabled()).isFalse();
    }

    @Test
    void replacePreferences_createsRow_whenNoneExists() {
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(repository.save(entityCaptor.capture())).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new NotificationPreferencesUpdateRequest(false, true, false);
        NotificationPreferencesResponse response = service.replacePreferences(USER_ID, request);

        assertThat(entityCaptor.getValue().getUserId()).isEqualTo(USER_ID);
        assertThat(response.emailEnabled()).isFalse();
        assertThat(response.smsEnabled()).isTrue();
        assertThat(response.pushEnabled()).isFalse();
    }

    @Test
    void replacePreferences_updatesExistingRow() {
        UserNotificationPreference existing = new UserNotificationPreference(USER_ID, true, false, true);
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new NotificationPreferencesUpdateRequest(false, false, false);
        NotificationPreferencesResponse response = service.replacePreferences(USER_ID, request);

        assertThat(response.emailEnabled()).isFalse();
        assertThat(response.smsEnabled()).isFalse();
        assertThat(response.pushEnabled()).isFalse();
    }

    @Test
    void patchPreferences_onlyChangesProvidedFields() {
        UserNotificationPreference existing = new UserNotificationPreference(USER_ID, true, false, true);
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new NotificationPreferencesPatchRequest(null, true, null);
        NotificationPreferencesResponse response = service.patchPreferences(USER_ID, request);

        assertThat(response.emailEnabled()).isTrue();
        assertThat(response.smsEnabled()).isTrue();
        assertThat(response.pushEnabled()).isTrue();
    }

    @Test
    void patchPreferences_seedsDefaults_whenNoRowExists() {
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var request = new NotificationPreferencesPatchRequest(null, true, null);
        NotificationPreferencesResponse response = service.patchPreferences(USER_ID, request);

        assertThat(response.emailEnabled()).isTrue();
        assertThat(response.smsEnabled()).isTrue();
        assertThat(response.pushEnabled()).isTrue();
    }

    @Test
    void replacePreferences_translatesOptimisticLockFailure() {
        when(repository.findByUserId(USER_ID)).thenReturn(Optional.empty());
        when(repository.save(any())).thenThrow(new ObjectOptimisticLockingFailureException(UserNotificationPreference.class, USER_ID));

        var request = new NotificationPreferencesUpdateRequest(true, true, true);

        assertThatThrownBy(() -> service.replacePreferences(USER_ID, request))
                .isInstanceOf(OptimisticLockConflictException.class);
    }
}
