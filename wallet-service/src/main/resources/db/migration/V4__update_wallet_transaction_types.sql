ALTER TABLE wallet_transactions
DROP CONSTRAINT chk_wallet_transaction_type;

ALTER TABLE wallet_transactions
ADD CONSTRAINT chk_wallet_transaction_type
CHECK (type IN ('DEPOSIT', 'WITHDRAWAL', 'DEBIT', 'CREDIT'));