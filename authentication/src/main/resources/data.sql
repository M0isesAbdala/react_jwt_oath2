INSERT IGNORE INTO users (id, email, password, username) VALUES(1, 'admin@example.com', '$2a$10$PCPb40R2aXZDcUPAUBTJt.S5wgujE.Q/f42YP6AsD8eCmVn2vgBf.', 'admin');

INSERT IGNORE INTO role (id, name) VALUES(1, 'ADMIN');
INSERT IGNORE INTO role(id, name) VALUES(2, 'USER');

INSERT IGNORE INTO users_roles (user_id, role_id) VALUES(1, 1);

INSERT IGNORE INTO oauth2_registered_client (
    id,
    client_id,
    client_id_issued_at,
    client_secret,
    client_secret_expires_at,
    client_name,
    client_authentication_methods,
    authorization_grant_types,
    redirect_uris,
    post_logout_redirect_uris,
    scopes,
    client_settings,
    token_settings
) VALUES (
    '1',
    'client',
    NULL,
    '$2a$10$sfJ6FsEae67K3ArZWb7bYezPw8zDmb98EQMXEbRxPV1SHWwfCsp32',
    NULL,
    'client',
    'client_secret_basic',
    'client_credentials,refresh_token',
    NULL,
    NULL,
    'ADMIN',
    '{"@class":"java.util.HashMap","settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.HashMap","settings.token.access-token-time-to-live":["java.time.Duration","P7D"],"settings.token.refresh-token-time-to-live":["java.time.Duration","PT24H"],"settings.token.authorization-code-time-to-live":["java.time.Duration","P7D"],"settings.token.reuse-refresh-tokens":true}'
);

INSERT IGNORE INTO oauth2_registered_client (
    id,
    client_id,
    client_id_issued_at,
    client_secret,
    client_secret_expires_at,
    client_name,
    client_authentication_methods,
    authorization_grant_types,
    redirect_uris,
    post_logout_redirect_uris,
    scopes,
    client_settings,
    token_settings
) VALUES (
    '2',
    'web-client',
    NULL,
    '$2a$10$sfJ6FsEae67K3ArZWb7bYezPw8zDmb98EQMXEbRxPV1SHWwfCsp32',
    NULL,
    'web-client',
    'client_secret_basic',
    'authorization_code,client_credentials,refresh_token',
    'http://mysite.com/client/login/oauth2/code/web-client-oidc',
    'http://mysite.com/client/logout',
    'openid,profile',
    '{"@class":"java.util.HashMap","settings.client.require-authorization-consent":true}',
    '{"@class":"java.util.HashMap","settings.token.access-token-time-to-live":["java.time.Duration","P7D"],"settings.token.refresh-token-time-to-live":["java.time.Duration","PT24H"],"settings.token.authorization-code-time-to-live":["java.time.Duration","P7D"],"settings.token.reuse-refresh-tokens":true}'
);

INSERT IGNORE INTO oauth2_registered_client (
    id,
    client_id,
    client_id_issued_at,
    client_secret,
    client_secret_expires_at,
    client_name,
    client_authentication_methods,
    authorization_grant_types,
    redirect_uris,
    post_logout_redirect_uris,
    scopes,
    client_settings,
    token_settings
) VALUES (
    '3',
    'swagger-client',
    NULL,
    NULL,
    NULL,
    'swagger-client',
    'none',
    'authorization_code,refresh_token',
    'http://mysite.com/resource/swagger-ui/oauth2-redirect.html',
     NULL,
     'openid,ADMIN',
     '{"@class":"java.util.HashMap","settings.client.require-proof-key":true,"settings.client.require-authorization-consent":true}', '{"@class":"java.util.HashMap","settings.token.access-token-time-to-live":["java.time.Duration","P7D"],"settings.token.refresh-token-time-to-live":["java.time.Duration","PT24H"],"settings.token.authorization-code-time-to-live":["java.time.Duration","P7D"],"settings.token.reuse-refresh-tokens":true}'
);