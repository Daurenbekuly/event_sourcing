package com.example.demo.entity;

import com.example.demo.model.Step;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table(value = "step")
public class StepEntity {

    @PrimaryKey(value = "step_id")
    private UUID stepId;
    @Column(value = "name")
    private String name;
    @Column(value = "receiver_name")
    private String receiverName;
    @Column(value = "json_value")
    private String jsonValue;
    @Column(value = "stat_date")
    private Instant statDate;
    @Column(value = "retry_count")
    private Integer retryCount;


    public StepEntity(UUID stepId, String name, String receiverName, String jsonValue, Instant statDate, Integer retryCount) {
        this.stepId = stepId;
        this.name = name;
        this.receiverName = receiverName;
        this.jsonValue = jsonValue;
        this.statDate = statDate;
        this.retryCount = retryCount;
    }

    public StepEntity(Step step) {
        this.stepId = UUID.randomUUID();
        this.name = step.name();
        this.receiverName = step.receiverName();
        this.jsonValue = step.jsonValue();
        this.statDate = step.statDate();
        this.retryCount = step.retryCount();
    }

    public StepEntity(UUID uuid, Step step, Integer availableTryCount) {
        this.stepId = uuid;
        this.name = step.name();
        this.receiverName = step.receiverName();
        this.jsonValue = step.jsonValue();
        this.statDate = step.statDate();
        this.retryCount = availableTryCount;
    }

    public UUID getStepId() {
        return stepId;
    }

    public void setStepId(UUID stepId) {
        this.stepId = stepId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getJsonValue() {
        return jsonValue;
    }

    public void setJsonValue(String jsonValue) {
        this.jsonValue = jsonValue;
    }

    public Instant getStatDate() {
        return statDate;
    }

    public void setStatDate(Instant statDate) {
        this.statDate = statDate;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
