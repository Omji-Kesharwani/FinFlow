ALTER TABLE transfers
DROP COLUMN sender_wallet_id;

ALTER TABLE transfers
DROP COLUMN receiver_wallet_id;

CREATE INDEX idx_transfers_receiver_user_id
    ON transfers(receiver_user_id);