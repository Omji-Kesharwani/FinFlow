ALTER TABLE payments
ADD COLUMN idempotency_key VARCHAR(100) NOT NULL;

ALTER TABLE payments
ADD CONSTRAINT uk_payments_idempotency_key
UNIQUE (idempotency_key);