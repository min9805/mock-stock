INSERT INTO users
    (o_auth_provider, user_role, created_at, id, updated_at, email, name, password)
VALUES
    ('0', '1', '2024-11-07 12:44:27.570979', '1', NULL, 'admin@admin.com', 'admin', '$2a$10$IEewf3yfCKXxRTavDLA48eDBZPAWvza3m6/d6zcsDTZh/kZuZj0KS');


INSERT INTO accounts
    (bitcoin_balance, krw_balance, usd_balance, account_id, user_id, account_number)
VALUES
    ('0', '1000000', '100000000', '1', '1', '1234-1234-12');