ALTER TABLE wallet_transactions
ADD COLUMN reference VARCHAR(100);

UPDATE wallet_transactions
SET reference = 'LEGACY-' || id::text
WHERE reference IS NULL;

ALTER TABLE wallet_transactions
ALTER COLUMN reference SET NOT NULL;

CREATE INDEX idx_wallet_transactions_reference
ON wallet_transactions(reference);