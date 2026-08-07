package com.company.notification.mapper;

import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.entity.UserNotificationPreference;
import org.springframework.stereotype.Component;

@Component
public class NotificationPreferencesMapper {

    public NotificationPreferencesResponse toResponse(UserNotificationPreference entity) {
        return new NotificationPreferencesResponse(
                entity.isEmailEnabled(),
                entity.isSmsEnabled(),
                entity.isPushEnabled(),
                entity.getUpdatedAt()
        );
    }

    public NotificationPreferencesResponse defaultResponse(boolean defaultEmail, boolean defaultSms, boolean defaultPush) {
        return new NotificationPreferencesResponse(defaultEmail, defaultSms, defaultPush, null);
    }
}
