package com.company.notification.service.impl;

import com.company.notification.dto.NotificationPreferencesPatchRequest;
import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.dto.NotificationPreferencesUpdateRequest;
import com.company.notification.entity.UserNotificationPreference;
import com.company.notification.exception.OptimisticLockConflictException;
import com.company.notification.mapper.NotificationPreferencesMapper;
import com.company.notification.repository.UserNotificationPreferencesRepository;
import com.company.notification.service.NotificationPreferencesService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

@Service
public class NotificationPreferencesServiceImpl implements NotificationPreferencesService {

    private static final boolean DEFAULT_EMAIL_ENABLED = true;
    private static final boolean DEFAULT_SMS_ENABLED = false;
    private static final boolean DEFAULT_PUSH_ENABLED = true;

    private final UserNotificationPreferencesRepository repository;
    private final NotificationPreferencesMapper mapper;

    public NotificationPreferencesServiceImpl(UserNotificationPreferencesRepository repository, NotificationPreferencesMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NotificationPreferencesResponse getPreferencesForUser(Long userId) {
        return repository.findByUserId(userId)
                .map(mapper::toResponse)
                .orElseGet(() -> mapper.defaultResponse(DEFAULT_EMAIL_ENABLED, DEFAULT_SMS_ENABLED, DEFAULT_PUSH_ENABLED));
    }

    @Override
    public NotificationPreferencesResponse replacePreferences(Long userId, NotificationPreferencesUpdateRequest request) {
        UserNotificationPreference entity = repository.findByUserId(userId)
                .orElseGet(() -> new UserNotificationPreference(userId, DEFAULT_EMAIL_ENABLED, DEFAULT_SMS_ENABLED, DEFAULT_PUSH_ENABLED));

        entity.setEmailEnabled(request.emailEnabled());
        entity.setSmsEnabled(request.smsEnabled());
        entity.setPushEnabled(request.pushEnabled());

        return mapper.toResponse(saveWithConflictHandling(entity));
    }

    @Override
    public NotificationPreferencesResponse patchPreferences(Long userId, NotificationPreferencesPatchRequest request) {
        UserNotificationPreference entity = repository.findByUserId(userId)
                .orElseGet(() -> new UserNotificationPreference(userId, DEFAULT_EMAIL_ENABLED, DEFAULT_SMS_ENABLED, DEFAULT_PUSH_ENABLED));

        if (request.emailEnabled() != null) {
            entity.setEmailEnabled(request.emailEnabled());
        }
        if (request.smsEnabled() != null) {
            entity.setSmsEnabled(request.smsEnabled());
        }
        if (request.pushEnabled() != null) {
            entity.setPushEnabled(request.pushEnabled());
        }

        return mapper.toResponse(saveWithConflictHandling(entity));
    }

    private UserNotificationPreference saveWithConflictHandling(UserNotificationPreference entity) {
        try {
            return repository.save(entity);
        } catch (ObjectOptimisticLockingFailureException ex) {
            throw new OptimisticLockConflictException("Preferences were updated elsewhere, please retry", ex);
        } catch (DataIntegrityViolationException ex) {
            throw new OptimisticLockConflictException("Preferences were created elsewhere, please retry", ex);
        }
    }
}
