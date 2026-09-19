CREATE TABLE dlt_events (
    id UUID PRIMARY KEY,
    payload TEXT NOT NULL,
    original_topic VARCHAR(255) NOT NULL,
    dlt_topic VARCHAR(255) NOT NULL,
    partition INTEGER NOT NULL,
    kafka_offset BIGINT NOT NULL,
    status VARCHAR(50) NOT NULL,
    replay_count INTEGER NOT NULL DEFAULT 0,
    failed_at TIMESTAMP WITH TIME ZONE NOT NULL,
    replayed_at TIMESTAMP WITH TIME ZONE
);