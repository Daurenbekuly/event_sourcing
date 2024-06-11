package com.example.demo.entity;

import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;

@Table(value = "stopped_step")
public class StoppedStepEntity {

    @PrimaryKeyColumn(value = "sashok_id", type = PrimaryKeyType.CLUSTERED)
    private Long sashokId;
    @PrimaryKeyColumn(value = "create_date", type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    private Instant createDate;

    public StoppedStepEntity(Long sashokId) {
        this.sashokId = sashokId;
        this.createDate = Instant.now();
    }

    public Long getSashokId() {
        return sashokId;
    }

    public void setSashokId(Long sashokId) {
        this.sashokId = sashokId;
    }

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }
}
