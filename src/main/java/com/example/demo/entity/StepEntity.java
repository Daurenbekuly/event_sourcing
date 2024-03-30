package com.example.demo.entity;

import com.example.demo.model.BaseModel;
import org.springframework.data.cassandra.core.cql.Ordering;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

import java.time.Instant;
import java.util.UUID;

@Table(value = "step")
public class StepEntity {

    @PrimaryKeyColumn(value = "step_id", type = PrimaryKeyType.CLUSTERED)
    private UUID stepId;
    @PrimaryKeyColumn(value = "sashok_id", type = PrimaryKeyType.PARTITIONED)
    private Integer sashokId;
    @Column(value = "name")
    private String name;
    @Column(value = "receiver_name")
    private String receiverName;
    @Column(value = "json_value")
    private String jsonValue;
    @PrimaryKeyColumn(value = "create_date", type = PrimaryKeyType.PARTITIONED, ordering = Ordering.DESCENDING)
    private Instant createDate;
    @Column(value = "retry_count")
    private Integer retryCount;


    public StepEntity(UUID stepId,
                      Integer sashokId,
                      String name,
                      String receiverName,
                      String jsonValue,
                      Instant createDate,
                      Integer retryCount) {
        this.stepId = stepId;
        this.sashokId = sashokId;
        this.name = name;
        this.receiverName = receiverName;
        this.jsonValue = jsonValue;
        this.createDate = createDate;
        this.retryCount = retryCount;
    }

    public StepEntity(BaseModel baseModel) {
        this.stepId = baseModel.stepId();
        this.sashokId = baseModel.sashokId();
        this.name = baseModel.name();
        this.receiverName = baseModel.receiverName();
        this.jsonValue = baseModel.jsonValue();
        this.createDate = baseModel.createDate();
        this.retryCount = baseModel.retryCount();
    }

    public UUID getStepId() {
        return stepId;
    }

    public void setStepId(UUID stepId) {
        this.stepId = stepId;
    }

    public Integer getSashokId() {
        return sashokId;
    }

    public void setSashokId(Integer sashokId) {
        this.sashokId = sashokId;
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

    public Instant getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Instant createDate) {
        this.createDate = createDate;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }
}
