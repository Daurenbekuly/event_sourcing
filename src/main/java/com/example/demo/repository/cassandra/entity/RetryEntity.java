package com.example.demo.repository.cassandra.entity;

import com.example.demo.route.model.BaseModel;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table(value = "retry")
public class RetryEntity {

    @PrimaryKeyColumn(value = "step_id", type = PrimaryKeyType.CLUSTERED)
    private UUID stepId;
    @Column(value = "create_date")
    private Instant createDate;
    @Column(value = "retry_date")
    private Instant retry_date;
    @PrimaryKeyColumn(value = "retry_count", type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    private Integer retryCount;

    public RetryEntity(Instant nextRetryDate) {
    }

    public RetryEntity(BaseModel newBaseModel, Instant nextRetryDate) {
        this.stepId = newBaseModel.stepId();
        this.createDate = Instant.now();
        this.retry_date = nextRetryDate;
        this.retryCount = newBaseModel.retryCount();
    }

    public UUID getStepId() {
        return stepId;
    }

    public void setStepId(UUID stepId) {
        this.stepId = stepId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Instant getRetry_date() {
        return retry_date;
    }

    public void setRetry_date(Instant retry_date) {
        this.retry_date = retry_date;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
