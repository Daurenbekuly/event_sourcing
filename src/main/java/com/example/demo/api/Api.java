package com.example.demo.api;

import com.example.demo.api.request.StartRequest;
import com.example.demo.common.JsonUtil;
import com.example.demo.demo.ListNode;
import com.example.demo.repository.cassandra.CassandraRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.repository.cassandra.entity.StoppedStepEntity;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.builder.Components;
import com.example.demo.route.builder.RouteBuilder;
import com.example.demo.route.model.BaseModel;
import com.example.demo.route.model.BuildRouteList;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;
import static com.example.demo.route.step.AbstractSashokStep.nameValidator;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/sashok")
public class Api {

    protected final Logger log = LogManager.getLogger(getClass());

    private final ProducerTemplate template;
    private final PostgresRepository postgresRepository;
    private final CassandraRepository cassandraRepository;
    private final RouteBuilder routeBuilder;
    private final Components components;

    public Api(ProducerTemplate template,
               PostgresRepository postgresRepository,
               CassandraRepository cassandraRepository,
               RouteBuilder routeBuilder,
               Components components) {
        this.template = template;
        this.postgresRepository = postgresRepository;
        this.cassandraRepository = cassandraRepository;
        this.routeBuilder = routeBuilder;
        this.components = components;
    }

    @PostMapping("/start")
    public ResponseEntity<?> callProcess(@RequestBody StartRequest request) {
        try {
            nameValidator(request.name());
            ListNode listNode = new ListNode(request.value());
            String jsonValue = JsonUtil.toJson(listNode).orElseThrow();
            UUID stepId = UUID.randomUUID();
            BaseModel baseModel = new BaseModel(stepId, "api:camel", request.name(), jsonValue);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(request.name(), json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), OK);
        }
    }

    @PostMapping("/jump/{sashokId}/{stepFrom}/{stepTo}")
    public ResponseEntity<?> jump(@PathVariable Long sashokId,
                                  @PathVariable String stepFrom,
                                  @PathVariable String stepTo) {
        try {
            String roadJson = postgresRepository.findRoadById(sashokId).orElseThrow();
            Map<String, UUID> road = JsonUtil.toCollection(roadJson, new TypeReference<Map<String, UUID>>() {
            }).orElseThrow();
            UUID stepId = road.get(stepFrom);
            StepEntity stepEntity = cassandraRepository.step().findFirstByStepIdAndCreateDateLessThan(stepId, LocalDateTime.now()).orElseThrow();
            UUID uuid = UUID.randomUUID();
            road.put(stepTo, uuid);
            BaseModel baseModel = new BaseModel(uuid, stepEntity.getSashokId(), stepEntity.getName(), stepTo, stepEntity.getJsonValue(), road);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(KAFKA_PATH_SASHOK, json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), OK);
        }
    }

    @PostMapping("/retry/{sashokId}/{stepId}")
    public ResponseEntity<?> retry(@PathVariable Long sashokId,
                                   @PathVariable UUID stepId) {
        try {
            String roadJson = postgresRepository.findRoadById(sashokId).orElseThrow();
            StepEntity stepEntity = cassandraRepository.step().findFirstByStepIdAndCreateDateLessThan(stepId, LocalDateTime.now()).orElseThrow();
            Map<String, UUID> road = JsonUtil.toCollection(roadJson, new TypeReference<Map<String, UUID>>() {
            }).orElseThrow();
            BaseModel baseModel = new BaseModel(stepEntity, road);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(stepEntity.getName(), json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), OK);
        }
    }

    @PostMapping("/cancel/{sashokId}")
    public ResponseEntity<?> cancel(@PathVariable Long sashokId) {
        cassandraRepository.stoppedStep().save(new StoppedStepEntity(sashokId));
        return ResponseEntity.ok().build();
    }

    @PostMapping("/build")
    public ResponseEntity<?> buildRoad(@RequestBody BuildRouteList buildRouteList) {
        try {
            routeBuilder.invoke(buildRouteList);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), OK);
        }
    }

    @GetMapping("/processors")
    public ResponseEntity<?> processors() {
        List<String> processors = components.getAllProcessors();
        return ResponseEntity.ok(processors);
    }

    @GetMapping("/steps")
    public ResponseEntity<?> steps() {
        List<Components.Steps> steps = components.getAllSteps();
        return ResponseEntity.ok(steps);
    }

}
