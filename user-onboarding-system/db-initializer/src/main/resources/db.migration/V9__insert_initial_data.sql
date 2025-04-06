SET search_path TO users_schema;

TRUNCATE TABLE password_history CASCADE;
TRUNCATE TABLE login_attempts CASCADE;
TRUNCATE TABLE oauth_users CASCADE;
TRUNCATE TABLE sessions CASCADE;
TRUNCATE TABLE user_roles CASCADE;
TRUNCATE TABLE users CASCADE;
TRUNCATE TABLE roles CASCADE;

-- Insert roles
INSERT INTO roles (name, description) VALUES
('ADMIN', 'Admin with full access'),
('USER', 'Standard user'),
('AUDITOR', 'Read-only access for audits');

-- Insert users
INSERT INTO users (id, username, email, password_hash, created_at)
VALUES
('11111111-1111-1111-1111-111111111111', 'alice', 'alice@example.com', 'hashed_password1', CURRENT_TIMESTAMP),
('22222222-2222-2222-2222-222222222222', 'bob', 'bob@example.com', 'hashed_password2', CURRENT_TIMESTAMP),
('33333333-3333-3333-3333-333333333333', 'carol', 'carol@example.com', 'hashed_password3', CURRENT_TIMESTAMP),
('44444444-4444-4444-4444-444444444444', 'dave', 'dave@example.com', 'hashed_password4', CURRENT_TIMESTAMP),
('55555555-5555-5555-5555-555555555555', 'eve', 'eve@example.com', 'hashed_password5', CURRENT_TIMESTAMP);

-- Assign roles
-- Alice: ADMIN
INSERT INTO user_roles (user_id, role_id)
SELECT '11111111-1111-1111-1111-111111111111', id FROM roles WHERE name = 'ADMIN';

-- Bob: USER + AUDITOR
INSERT INTO user_roles (user_id, role_id)
SELECT '22222222-2222-2222-2222-222222222222', id FROM roles WHERE name IN ('USER', 'AUDITOR');

-- Carol: ADMIN + USER + AUDITOR
INSERT INTO user_roles (user_id, role_id)
SELECT '33333333-3333-3333-3333-333333333333', id FROM roles WHERE name IN ('ADMIN', 'USER', 'AUDITOR');

-- Dave: USER
INSERT INTO user_roles (user_id, role_id)
SELECT '44444444-4444-4444-4444-444444444444', id FROM roles WHERE name = 'USER';

-- Eve: AUDITOR
INSERT INTO user_roles (user_id, role_id)
SELECT '55555555-5555-5555-5555-555555555555', id FROM roles WHERE name = 'AUDITOR';

-- Sessions
INSERT INTO sessions (id, user_id, jwt_token, issued_at, expires_at, ip_address, user_agent)
VALUES
(gen_random_uuid(), '11111111-1111-1111-1111-111111111111', 'jwt_token_1', NOW(), NOW() + interval '1 day', '192.168.0.1', 'Chrome on Windows'),
(gen_random_uuid(), '22222222-2222-2222-2222-222222222222', 'jwt_token_2', NOW(), NOW() + interval '1 day', '192.168.0.2', 'Firefox on Mac'),
(gen_random_uuid(), '33333333-3333-3333-3333-333333333333', 'jwt_token_3', NOW(), NOW() + interval '1 day', '192.168.0.3', 'Edge on Linux');

-- OAuth users
INSERT INTO oauth_users (id, user_id, external_provider, external_user_id, access_token, refresh_token, token_expiry)
VALUES
(gen_random_uuid(), '11111111-1111-1111-1111-111111111111', 'google', 'google-uid-1111', 'access_token_1', 'refresh_token_1', NOW() + interval '1 day'),
(gen_random_uuid(), '22222222-2222-2222-2222-222222222222', 'facebook', 'fb-uid-2222', 'access_token_2', 'refresh_token_2', NOW() + interval '1 day');

-- Login attempts
INSERT INTO login_attempts (user_id, email, success, ip_address, user_agent)
VALUES
('11111111-1111-1111-1111-111111111111', 'alice@example.com', TRUE, '192.168.0.1', 'Chrome'),
(NULL, 'unknown@example.com', FALSE, '192.168.0.5', 'Safari'),
('33333333-3333-3333-3333-333333333333', 'carol@example.com', FALSE, '192.168.0.3', 'Edge');

-- Password history
INSERT INTO password_history (user_id, password_hash, changed_at)
VALUES
('11111111-1111-1111-1111-111111111111', 'old_hash_1', NOW() - interval '90 days'),
('22222222-2222-2222-2222-222222222222', 'old_hash_2', NOW() - interval '45 days'),
('33333333-3333-3333-3333-333333333333', 'old_hash_3', NOW() - interval '30 days');