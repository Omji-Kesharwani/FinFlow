CREATE TABLE transfers (
    id UUID PRIMARY KEY,

    sender_user_id UUID NOT NULL,

    sender_wallet_id UUID NOT NULL,

    receiver_user_id UUID NOT NULL ,

    receiver_wallet_id UUID NOT NULL,

    amount DECIMAL(19,4) NOT NULL,

    currency VARCHAR(3) NOT NULL,

    status VARCHAR(20) NOT NULL,

    reference VARCHAR(100) NOT NULL,

    idempotency_key VARCHAR(100) NOT NULL,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT uk_transfer_reference
        UNIQUE (reference),

    CONSTRAINT uk_transfer_idempotency_key
        UNIQUE (idempotency_key),

    CONSTRAINT chk_transfer_status
        CHECK (status IN ('PENDING', 'COMPLETED', 'FAILED')),

    CONSTRAINT chk_transfer_amount
        CHECK (amount > 0)
);

CREATE INDEX idx_transfers_sender_user_id
    ON transfers(sender_user_id);

CREATE INDEX idx_transfers_sender_wallet_id
    ON transfers(sender_wallet_id);

CREATE INDEX idx_transfers_receiver_wallet_id
    ON transfers(receiver_wallet_id);

CREATE INDEX idx_transfers_created_at
    ON transfers(created_at);