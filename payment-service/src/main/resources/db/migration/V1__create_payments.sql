CREATE TABLE payments (
    id UUID PRIMARY KEY,

    user_id UUID NOT NULL,

    wallet_id UUID NOT NULL,

    amount DECIMAL(19,4) NOT NULL,

    currency VARCHAR(3) NOT NULL,

    status VARCHAR(20) NOT NULL,

    reference VARCHAR(100) NOT NULL UNIQUE,

    created_at TIMESTAMP WITH TIME ZONE NOT NULL,

    updated_at TIMESTAMP WITH TIME ZONE NOT NULL,

    CONSTRAINT chk_payment_amount
        CHECK (amount > 0),

    CONSTRAINT chk_payment_currency
        CHECK (currency IN ('INR', 'USD', 'EUR')),

    CONSTRAINT chk_payment_status
        CHECK (
            status IN (
                'PENDING',
                'PROCESSING',
                'COMPLETED',
                'FAILED'
            )
        )
);

CREATE INDEX idx_payments_user_id
    ON payments(user_id);

CREATE INDEX idx_payments_wallet_id
    ON payments(wallet_id);

CREATE INDEX idx_payments_status
    ON payments(status);

CREATE INDEX idx_payments_created_at
    ON payments(created_at);