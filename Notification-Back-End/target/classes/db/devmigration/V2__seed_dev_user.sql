-- Seeds the fixed dev user that DevAuthenticationFilter authenticates every
-- request as, so PUT/PATCH on /api/v1/notification-preferences satisfy the
-- FK to users(id) during local development.
INSERT INTO users (id, email) VALUES (1, 'dev-user@example.local');
