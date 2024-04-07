package com.example.demo.model;

import com.example.demo.entity.StepEntity;

import java.time.Instant;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public record BaseModel(
        UUID stepId,
        Long sashokId,
        String name,
        String receiverName,
        String jsonValue,
        Instant createDate,
        Integer retryCount,
        Map<String, UUID> road,
        Stack<String> mainRoadSteps) {

    public BaseModel(BaseModel baseModel, UUID stepId, String receiver, Integer retryCount) {
        this(stepId,
                baseModel.sashokId(),
                baseModel.receiverName(),
                receiver,
                baseModel.jsonValue(),
                baseModel.createDate(),
                retryCount,
                baseModel.road(),
                baseModel.mainRoadSteps()
        );
    }

    public BaseModel(UUID uuid, String name, String receiverName, String jsonValue, Map<String, UUID> map) {
        this(uuid,
                0L,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                5,
                map,
                new Stack<>());
    }

    public BaseModel(UUID uuid, Long sashokId, String name, String receiverName, String jsonValue, Map<String, UUID> map) {
        this(uuid,
                sashokId,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                5,
                map,
                new Stack<>());
    }

    public BaseModel(BaseModel baseModel, Long sashokId) {
        this(UUID.randomUUID(),
                sashokId,
                baseModel.name,
                baseModel.receiverName,
                baseModel.jsonValue,
                Instant.now(),
                5,
                baseModel.road,
                baseModel.mainRoadSteps);
    }

    public BaseModel(BaseModel baseModel, String receiver, Stack<String> mainRoadSteps) {
        this(baseModel.stepId,
                baseModel.sashokId,
                baseModel.name,
                receiver,
                baseModel.jsonValue,
                Instant.now(),
                5,
                baseModel.road,
                mainRoadSteps);
    }

    public BaseModel(BaseModel baseModel, String receiver) {
        this(baseModel.stepId,
                baseModel.sashokId,
                baseModel.name,
                receiver,
                baseModel.jsonValue,
                Instant.now(),
                5,
                baseModel.road,
                baseModel.mainRoadSteps);
    }

    public BaseModel(BaseModel baseModel, UUID uuid, String receiver, String jsonValue, Map<String, UUID> road) {
        this(uuid,
                baseModel.sashokId,
                baseModel.receiverName,
                receiver,
                jsonValue,
                Instant.now(),
                5,
                road,
                baseModel.mainRoadSteps);
    }

    public BaseModel(StepEntity stepEntity, Map<String, UUID> road) {
        this(stepEntity.getStepId(),
                stepEntity.getSashokId(),
                stepEntity.getName(),
                stepEntity.getReceiverName(),
                stepEntity.getJsonValue(),
                stepEntity.getCreateDate(),
                5,
                road,
                stepEntity.getMainRoadSteps());
    }
}
