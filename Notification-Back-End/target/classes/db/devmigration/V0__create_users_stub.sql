-- Dev-only stand-in for the real users table, which lives in another
-- bounded context and is out of scope for this service. Exists purely so
-- the FK in V1__create_user_notification_preferences.sql has something to
-- reference during local development.
--
-- H2's PostgreSQL compatibility mode doesn't recognize TIMESTAMPTZ, so alias
-- it to H2's native TIMESTAMP WITH TIME ZONE for the shared migration below.
CREATE DOMAIN IF NOT EXISTS TIMESTAMPTZ AS TIMESTAMP WITH TIME ZONE;

CREATE TABLE users (
    id    BIGSERIAL PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE
);
