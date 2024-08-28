package com.example.demo.route.model;

import com.example.demo.repository.cassandra.entity.StepEntity;

import java.time.Instant;
import java.util.Map;
import java.util.Stack;
import java.util.UUID;

/**
 * Transfer process information
 *
 * @param stepId
 * @param sashokId
 * @param name
 * @param receiverName
 * @param jsonValue
 * @param createDate
 * @param retryCount
 * @param route map with step name and step id
 * @param mainRouteSteps stack next main route step name
 */
public record BaseModel(
        UUID stepId,
        Long sashokId,
        String name,
        String receiverName,
        String jsonValue,
        Instant createDate,
        Integer retryCount,
        Map<String, UUID> route,
        Stack<String> mainRouteSteps) {

    public BaseModel(BaseModel baseModel, UUID stepId, String receiver, Integer retryCount) {
        this(stepId,
                baseModel.sashokId(),
                baseModel.receiverName(),
                receiver,
                baseModel.jsonValue(),
                baseModel.createDate(),
                retryCount,
                baseModel.route(),
                baseModel.mainRouteSteps()
        );
    }

    public BaseModel(UUID uuid, String name, String receiverName, String jsonValue) {
        this(uuid,
                0L,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                -1,
                Map.of(receiverName, uuid),
                new Stack<>());
    }

    public BaseModel(UUID uuid, Long sashokId, String name, String receiverName, String jsonValue, Map<String, UUID> route) {
        this(uuid,
                sashokId,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                -1,
                route,
                new Stack<>());
    }

    public BaseModel(BaseModel baseModel, Long sashokId) {
        this(UUID.randomUUID(),
                sashokId,
                baseModel.name,
                baseModel.receiverName,
                baseModel.jsonValue,
                Instant.now(),
                -1,
                baseModel.route,
                baseModel.mainRouteSteps);
    }

    public BaseModel(BaseModel baseModel, String receiver, Stack<String> mainRouteSteps) {
        this(baseModel.stepId,
                baseModel.sashokId,
                baseModel.name,
                receiver,
                baseModel.jsonValue,
                Instant.now(),
                -1,
                baseModel.route,
                mainRouteSteps);
    }

    public BaseModel(BaseModel baseModel, String receiver) {
        this(baseModel.stepId,
                baseModel.sashokId,
                baseModel.name,
                receiver,
                baseModel.jsonValue,
                Instant.now(),
                -1,
                baseModel.route,
                baseModel.mainRouteSteps);
    }

    public BaseModel(BaseModel baseModel, UUID uuid, String receiver, String jsonValue, Map<String, UUID> route) {
        this(uuid,
                baseModel.sashokId,
                baseModel.receiverName,
                receiver,
                jsonValue,
                Instant.now(),
                -1,
                route,
                baseModel.mainRouteSteps);
    }

    public BaseModel(StepEntity stepEntity, Map<String, UUID> route) {
        this(stepEntity.getStepId(),
                stepEntity.getSashokId(),
                stepEntity.getName(),
                stepEntity.getName(),
                stepEntity.getJsonValue(),
                stepEntity.getCreateDate(),
                -1,
                route,
                stepEntity.getMainRouteSteps());
    }

    public BaseModel(StepEntity stepEntity, String jsonValue, Map<String, UUID> route) {
        this(stepEntity.getStepId(),
                stepEntity.getSashokId(),
                stepEntity.getName(),
                stepEntity.getName(),
                jsonValue,
                stepEntity.getCreateDate(),
                -1,
                route,
                stepEntity.getMainRouteSteps());
    }
}
