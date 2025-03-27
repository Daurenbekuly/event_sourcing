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
 * @param passedRoute map with step name and step id
 * @param mainRouteSteps stack next main route step name
 */
public record BaseModel(
        UUID stepId,
        Long sashokId,
        String name,
        String receiverName,
        String jsonValue,
        Instant createDate,
        Map<String, UUID> passedRoute,
        Stack<String> mainRouteSteps) {

    public BaseModel(BaseModel baseModel, UUID stepId, String receiver, Integer retryCount) {
        this(stepId,
                baseModel.sashokId(),
                baseModel.receiverName(),
                receiver,
                baseModel.jsonValue(),
                baseModel.createDate(),
                baseModel.passedRoute(),
                baseModel.mainRouteSteps()
        );
    }

    public BaseModel(UUID stepId, String name, String receiverName, String jsonValue) {
        this(stepId,
                0L,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                Map.of(receiverName, stepId),
                new Stack<>());
    }

    public BaseModel(UUID stepId, Long sashokId, String name, String receiverName, String jsonValue, Map<String, UUID> passedRoute) {
        this(stepId,
                sashokId,
                name,
                receiverName,
                jsonValue,
                Instant.now(),
                passedRoute,
                new Stack<>());
    }

    public BaseModel(BaseModel baseModel, Long sashokId) {
        this(UUID.randomUUID(),
                sashokId,
                baseModel.name,
                baseModel.receiverName,
                baseModel.jsonValue,
                Instant.now(),
                baseModel.passedRoute,
                baseModel.mainRouteSteps);
    }

    public BaseModel(BaseModel baseModel, String receiver, Stack<String> mainRouteSteps) {
        this(baseModel.stepId,
                baseModel.sashokId,
                baseModel.name,
                receiver,
                baseModel.jsonValue,
                Instant.now(),
                baseModel.passedRoute,
                mainRouteSteps);
    }

    public BaseModel(BaseModel baseModel, String receiver) {
        this(baseModel.stepId,
                baseModel.sashokId,
                baseModel.name,
                receiver,
                baseModel.jsonValue,
                Instant.now(),
                baseModel.passedRoute,
                baseModel.mainRouteSteps);
    }

    public BaseModel(BaseModel baseModel, UUID stepId, String receiver, String jsonValue) {
        this(stepId,
                baseModel.sashokId,
                baseModel.receiverName,
                receiver,
                jsonValue,
                Instant.now(),
                baseModel.passedRoute,
                baseModel.mainRouteSteps);
    }

    public BaseModel(StepEntity stepEntity, Map<String, UUID> passedRoute) {
        this(stepEntity.getStepId(),
                stepEntity.getSashokId(),
                stepEntity.getName(),
                stepEntity.getName(),
                stepEntity.getJsonValue(),
                stepEntity.getCreateDate(),
                passedRoute,
                stepEntity.getMainRouteSteps());
    }

    public BaseModel(StepEntity stepEntity, String jsonValue, Map<String, UUID> passedRoute) {
        this(stepEntity.getStepId(),
                stepEntity.getSashokId(),
                stepEntity.getName(),
                stepEntity.getName(),
                jsonValue,
                stepEntity.getCreateDate(),
                passedRoute,
                stepEntity.getMainRouteSteps());
    }
}
