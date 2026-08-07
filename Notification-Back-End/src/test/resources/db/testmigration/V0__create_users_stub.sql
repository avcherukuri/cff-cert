-- Test-only stand-in for the real users table, which lives in another
-- bounded context and is out of scope for this service. Exists purely so
-- the FK in V1__create_user_notification_preferences.sql has something to
-- reference during integration tests.
CREATE TABLE users (
    id    BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
);
