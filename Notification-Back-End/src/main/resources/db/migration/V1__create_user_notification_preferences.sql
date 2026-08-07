-- Mirrors Notification-Data-Base/migrations/V1__create_user_notification_preferences.sql
-- Kept here too so Spring Boot's Flyway integration can auto-apply it on startup.
CREATE TABLE user_notification_preferences (
    id             BIGSERIAL PRIMARY KEY,
    user_id        BIGINT NOT NULL,
    email_enabled  BOOLEAN NOT NULL DEFAULT TRUE,
    sms_enabled    BOOLEAN NOT NULL DEFAULT FALSE,
    push_enabled   BOOLEAN NOT NULL DEFAULT TRUE,
    created_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    updated_at     TIMESTAMPTZ NOT NULL DEFAULT now(),
    version        BIGINT NOT NULL DEFAULT 0,

    CONSTRAINT fk_user_notification_preferences_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT uq_user_notification_preferences_user UNIQUE (user_id)
);

CREATE INDEX idx_user_notification_preferences_user_id
    ON user_notification_preferences (user_id);
