package com.example.demo.api;

import com.example.demo.api.request.ContinueRequest;
import com.example.demo.api.request.StartRequest;
import com.example.demo.repository.cassandra.CassandraRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.builder.Components;
import com.example.demo.route.builder.RouteBuilder;
import com.example.demo.route.model.BaseModel;
import com.example.demo.route.model.BuildRouteData;
import com.example.demo.route.model.RetryData;
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

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toTypeOrElseThrow;
import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;
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
            String firstStep = postgresRepository.findFirstStepOrElseThrow(request.name());
            String jsonValue = toJsonOrElseThrow(request.value());
            UUID stepId = UUID.randomUUID();
            BaseModel baseModel = new BaseModel(stepId, "api:camel", firstStep, jsonValue);
            String json = toJsonOrElseThrow(baseModel);
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
            String passedRouteJson = postgresRepository.findPassedRouteByIdOrElseThrow(sashokId);
            Map<String, UUID> passedRoute = toTypeOrElseThrow(passedRouteJson, new TypeReference<>() {});
            UUID stepId = passedRoute.get(stepFrom);
            StepEntity stepEntity = cassandraRepository.findFirstByStepIdOrElseThrow(stepId);
            UUID uuid = UUID.randomUUID();
            passedRoute.put(stepTo, uuid);
            BaseModel baseModel = new BaseModel(uuid, stepEntity.getSashokId(), stepEntity.getName(), stepTo, stepEntity.getJsonValue(), passedRoute);
            String json = toJsonOrElseThrow(baseModel);
            template.asyncRequestBody(KAFKA_PATH_SASHOK, json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/retry/{stepId}")
    public ResponseEntity<?> retryStep(@PathVariable UUID stepId) {
        try {
            StepEntity stepEntity = cassandraRepository.findFirstByStepIdOrElseThrow(stepId);
            String passedRouteJson = postgresRepository.findPassedRouteByIdOrElseThrow(stepEntity.getSashokId());
            Map<String, UUID> passedRoute = toTypeOrElseThrow(passedRouteJson, new TypeReference<>() {});
            BaseModel baseModel = new BaseModel(stepEntity, passedRoute);
            String json = toJsonOrElseThrow(baseModel);
            template.asyncRequestBody(stepEntity.getName(), json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/cancel/{sashokId}")
    public ResponseEntity<?> cancel(@PathVariable Long sashokId) {
        postgresRepository.tryCancel(sashokId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/build")
    public ResponseEntity<?> buildRoute(@RequestBody BuildRouteData buildRouteData) {
        try {
            Integer version = postgresRepository.findRouteLastVersion(buildRouteData.name()) + 1;
            routeBuilder.invoke(buildRouteData, version);
            postgresRepository.saveRoute(buildRouteData, version);
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
        List<String> steps = components.getAllSteps();
        return ResponseEntity.ok(steps);
    }

    @PostMapping("/continue/user-task")
    public ResponseEntity<?> continueUserTask(@RequestBody ContinueRequest request) {
        try {
            StepEntity stepEntity = cassandraRepository.findFirstByStepIdOrElseThrow(request.stepId());
            String passedRouteJson = postgresRepository.findPassedRouteByIdOrElseThrow(stepEntity.getSashokId());
            String jsonValue = toJsonOrElseThrow(request.value());
            Map<String, UUID> passedRoute = toTypeOrElseThrow(passedRouteJson, new TypeReference<>() {});
            BaseModel baseModel = new BaseModel(stepEntity, jsonValue, passedRoute);
            String json = toJsonOrElseThrow(baseModel);
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

    @PostMapping("/retry")
    public ResponseEntity<?> retry() {
        try {
            List<RetryData> retries = postgresRepository.findTop100ActiveRetries();
            retries.forEach(retry -> template.asyncRequestBody(KAFKA_PATH_SASHOK, retry.json()));
            postgresRepository.deactivateRetries(retries);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), INTERNAL_SERVER_ERROR);
        }
    }
}
