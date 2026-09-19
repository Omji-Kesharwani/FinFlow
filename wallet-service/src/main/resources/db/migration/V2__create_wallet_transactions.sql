CREATE TABLE wallet_transactions (
    id UUID PRIMARY KEY,

    wallet_id UUID NOT NULL,

    type VARCHAR(20) NOT NULL,

    amount DECIMAL(19,4) NOT NULL,

    balance_after DECIMAL(19,4) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_wallet_transaction_amount
        CHECK (amount > 0),

    CONSTRAINT chk_wallet_transaction_type
        CHECK (type IN ('DEPOSIT', 'WITHDRAWAL'))
);

CREATE INDEX idx_wallet_transactions_wallet_id
    ON wallet_transactions(wallet_id);

CREATE INDEX idx_wallet_transactions_created_at
    ON wallet_transactions(created_at);