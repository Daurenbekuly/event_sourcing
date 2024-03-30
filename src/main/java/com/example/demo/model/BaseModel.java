package com.example.demo.model;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

public record BaseModel(
        UUID stepId,
        Integer sashokId,
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
                baseModel.name(),
                receiver,
                baseModel.jsonValue(),
                Instant.now(),
                retryCount,
                baseModel.road(),
                baseModel.mainRoadSteps()
        );
    }

    public BaseModel(UUID uuid, String name, String receiverName, String jsonValue, Map<String, UUID> map) {
        this(uuid,
                null,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                5,
                map,
                new Stack<>());
    }

    public BaseModel(BaseModel baseModel, Integer sashokId) {
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
        this(UUID.randomUUID(),
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
        this(UUID.randomUUID(),
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
                baseModel.name,
                receiver,
                jsonValue,
                Instant.now(),
                5,
                road,
                baseModel.mainRoadSteps);
    }
}
