-- ==========================
-- Seed Accounts (100 accounts)
-- ==========================
INSERT INTO accounts (account_number, holder_name)
SELECT
    'ACC' || LPAD(gs::text, 5, '0') AS account_number,
    'User ' || gs AS holder_name
FROM generate_series(1, 100) gs;

-- ==========================
-- Seed Transactions (1000 transactions)
-- ==========================
INSERT INTO transactions (account_id, amount, type, timestamp)
SELECT
    (trunc(random() * 100) + 1)::int AS account_id,
    ROUND((random() * 5000 + 10)::numeric, 2) AS amount,
    CASE
        WHEN random() < 0.4 THEN 'DEPOSIT'
        WHEN random() < 0.7 THEN 'WITHDRAWAL'
        ELSE 'TRANSFER'
        END AS type,
    NOW() - (trunc(random() * 60) || ' DAY')::interval AS created_at
FROM generate_series(1, 1000);

-- ==========================
-- Optional: Index for faster queries
-- ==========================
CREATE INDEX idx_transactions_account_id ON transactions(account_id);
CREATE INDEX idx_transactions_created_at ON transactions(timestamp);


SELECT
    column_name,
    data_type,
    character_maximum_length,
    is_nullable,
    column_default
FROM
    information_schema.columns
WHERE
    table_name = 'accounts';
