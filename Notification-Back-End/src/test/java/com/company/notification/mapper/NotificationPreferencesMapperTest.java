package com.company.notification.mapper;

import com.company.notification.dto.NotificationPreferencesResponse;
import com.company.notification.entity.UserNotificationPreference;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationPreferencesMapperTest {

    private final NotificationPreferencesMapper mapper = new NotificationPreferencesMapper();

    @Test
    void toResponse_mapsAllFieldsFromEntity() throws Exception {
        UserNotificationPreference entity = new UserNotificationPreference(42L, true, true, false);
        setUpdatedAt(entity, Instant.parse("2026-08-05T18:30:00Z"));

        NotificationPreferencesResponse response = mapper.toResponse(entity);

        assertThat(response.emailEnabled()).isTrue();
        assertThat(response.smsEnabled()).isTrue();
        assertThat(response.pushEnabled()).isFalse();
        assertThat(response.updatedAt()).isEqualTo(Instant.parse("2026-08-05T18:30:00Z"));
    }

    @Test
    void defaultResponse_hasNullUpdatedAt() {
        NotificationPreferencesResponse response = mapper.defaultResponse(true, false, true);

        assertThat(response.emailEnabled()).isTrue();
        assertThat(response.smsEnabled()).isFalse();
        assertThat(response.pushEnabled()).isTrue();
        assertThat(response.updatedAt()).isNull();
    }

    private void setUpdatedAt(UserNotificationPreference entity, Instant updatedAt) throws Exception {
        Field field = UserNotificationPreference.class.getDeclaredField("updatedAt");
        field.setAccessible(true);
        field.set(entity, updatedAt);
    }
}
