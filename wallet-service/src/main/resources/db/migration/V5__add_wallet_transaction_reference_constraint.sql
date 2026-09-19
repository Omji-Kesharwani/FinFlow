ALTER TABLE wallet_transactions
ADD CONSTRAINT uk_wallet_transaction_wallet_reference
UNIQUE (wallet_id, reference);