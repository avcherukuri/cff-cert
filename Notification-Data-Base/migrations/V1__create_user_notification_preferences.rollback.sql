-- Rollback for V1__create_user_notification_preferences.sql
-- Safe: this table is net-new and nothing else in the system references it.
DROP TABLE IF EXISTS user_notification_preferences;
