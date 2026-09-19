package com.finflow.notification_service.entity;

import jakarta.persistence.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "dlt_events")
public class DltEvent {

    @Id
    private UUID id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String payload;

    @Column(nullable = false)
    private String originalTopic;

    @Column(nullable = false)
    private String dltTopic;

    @Column(nullable = false)
    private Integer partition;

    @Column(name = "kafka_offset", nullable = false)
    private Long offset;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private Integer replayCount;

    @Column(nullable = false)
    private OffsetDateTime failedAt;

    private OffsetDateTime replayedAt;

    protected DltEvent() {
    }

    public DltEvent(
            UUID id,
            String payload,
            String originalTopic,
            String dltTopic,
            Integer partition,
            Long offset,
            String status,
            Integer replayCount,
            OffsetDateTime failedAt
    ) {
        this.id = id;
        this.payload = payload;
        this.originalTopic = originalTopic;
        this.dltTopic = dltTopic;
        this.partition = partition;
        this.offset = offset;
        this.status = status;
        this.replayCount = replayCount;
        this.failedAt = failedAt;
    }

    public UUID getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    public String getOriginalTopic() {
        return originalTopic;
    }

    public String getDltTopic() {
        return dltTopic;
    }

    public Integer getPartition() {
        return partition;
    }

    public Long getOffset() {
        return offset;
    }

    public String getStatus() {
        return status;
    }

    public Integer getReplayCount() {
        return replayCount;
    }

    public OffsetDateTime getFailedAt() {
        return failedAt;
    }

    public OffsetDateTime getReplayedAt() {
        return replayedAt;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setReplayCount(Integer replayCount) {
        this.replayCount = replayCount;
    }

    public void setReplayedAt(OffsetDateTime replayedAt) {
        this.replayedAt = replayedAt;
    }
}