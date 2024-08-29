package com.example.demo.api;

import com.example.demo.api.request.ContinueRequest;
import com.example.demo.api.request.StartRequest;
import com.example.demo.common.JsonUtil;
import com.example.demo.repository.cassandra.CassandraRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.builder.Components;
import com.example.demo.route.builder.RouteBuilder;
import com.example.demo.route.model.BaseModel;
import com.example.demo.route.model.BuildRouteData;
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
import static com.example.demo.route.builder.Components.Steps.FIRST_STEP;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
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
    public ResponseEntity<?> start(@RequestBody StartRequest request) {
        try {
            String firstStep = postgresRepository.findRouteFirstStepByName(request.name());
            String jsonValue = JsonUtil.toJson(request.value()).orElseThrow();
            UUID stepId = UUID.randomUUID();
            BaseModel baseModel = new BaseModel(stepId, "api:camel", firstStep, jsonValue);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(firstStep, json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/jump/{sashokId}/{stepFrom}/{stepTo}")
    public ResponseEntity<?> jump(@PathVariable Long sashokId,
                                  @PathVariable String stepFrom,
                                  @PathVariable String stepTo) {
        try {
            String passedRouteJson = postgresRepository.findPassedRouteById(sashokId).orElseThrow();
            Map<String, UUID> passedRoute = JsonUtil.toCollection(passedRouteJson, new TypeReference<Map<String, UUID>>() {}).orElseThrow();
            UUID stepId = passedRoute.get(stepFrom);
            StepEntity stepEntity = cassandraRepository.step().findFirstByStepIdAndCreateDateLessThan(stepId, LocalDateTime.now()).orElseThrow();
            UUID uuid = UUID.randomUUID();
            passedRoute.put(stepTo, uuid);
            BaseModel baseModel = new BaseModel(uuid, stepEntity.getSashokId(), stepEntity.getName(), stepTo, stepEntity.getJsonValue(), passedRoute);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(KAFKA_PATH_SASHOK, json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/retry/{stepId}")
    public ResponseEntity<?> retry(@PathVariable UUID stepId) {
        try {
            StepEntity stepEntity = cassandraRepository.step().findFirstByStepIdAndCreateDateLessThan(stepId, LocalDateTime.now()).orElseThrow();
            String passedRouteJson = postgresRepository.findPassedRouteById(stepEntity.getSashokId()).orElseThrow();
            Map<String, UUID> passedRoute = JsonUtil.toCollection(passedRouteJson, new TypeReference<Map<String, UUID>>() {}).orElseThrow();
            BaseModel baseModel = new BaseModel(stepEntity, passedRoute);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(stepEntity.getName(), json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel/{sashokId}")
    public ResponseEntity<?> cancel(@PathVariable Long sashokId) {
        postgresRepository.tryCancelled(sashokId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/build")
    public ResponseEntity<?> buildRoute(@RequestBody BuildRouteData buildRouteData) {
        try {
            routeBuilder.invoke(buildRouteData);
            var firstStep = (String) buildRouteData
                    .steps()
                    .stream()
                    .filter(step -> FIRST_STEP.equals(step.key()))
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("First step is not exist"))
                    .value()
                    .get("name");
            postgresRepository.saveRoute(buildRouteData, firstStep);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/processors")
    public ResponseEntity<?> getAllProcessors() {
        List<String> processors = components.getAllProcessors();
        return ResponseEntity.ok(processors);
    }

    @GetMapping("/steps")
    public ResponseEntity<?> getAllSteps() {
        List<Components.Steps> steps = components.getAllSteps();
        return ResponseEntity.ok(steps);
    }

    @PostMapping("/continue/user-task")
    public ResponseEntity<?> continueUserTask(@RequestBody ContinueRequest request) {
        try {
            StepEntity stepEntity = cassandraRepository.step().findFirstByStepIdAndCreateDateLessThan(request.stepId(), LocalDateTime.now()).orElseThrow();
            String passedRouteJson = postgresRepository.findPassedRouteById(stepEntity.getSashokId()).orElseThrow();
            String jsonValue = JsonUtil.toJson(request.value()).orElseThrow();
            Map<String, UUID> passedRoute = JsonUtil.toCollection(passedRouteJson, new TypeReference<Map<String, UUID>>() {}).orElseThrow();
            BaseModel baseModel = new BaseModel(stepEntity, jsonValue, passedRoute);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(stepEntity.getReceiverName(), json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/route/{name}/deactivate")
    public ResponseEntity<?> deactivateRoute(@PathVariable String name) {
        postgresRepository.deactivateRoute(name);
        return new ResponseEntity<>(OK);
    }

}
