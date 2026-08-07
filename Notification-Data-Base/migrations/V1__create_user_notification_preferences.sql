-- Creates the notification preferences table for authenticated users.
-- Assumption: users.id is BIGINT. Confirm against the real users schema before
-- running this migration; switch user_id to UUID here (and in the FK) if wrong.
-- This migration only reads the users table via FK reference; it never alters it.

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
