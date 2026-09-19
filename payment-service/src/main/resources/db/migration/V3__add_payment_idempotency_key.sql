ALTER TABLE payments
ADD CONSTRAINT uk_payment_user_idempotency
UNIQUE (user_id, idempotency_key);