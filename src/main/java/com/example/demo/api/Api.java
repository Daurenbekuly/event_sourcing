package com.example.demo.api;

import com.example.demo.demo.ListNode;
import com.example.demo.repository.cassandra.CassandraRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.repository.cassandra.entity.StoppedStepEntity;
import com.example.demo.route.model.BaseModel;
import com.example.demo.demo.R1Config;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.common.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import org.apache.camel.ProducerTemplate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/camel")
public class Api {

    protected final Logger log = LogManager.getLogger(getClass());

    private final ProducerTemplate template;
    private final R1Config r1Config;
    private final PostgresRepository postgresRepository;
    private final CassandraRepository cassandraRepository;

    public Api(ProducerTemplate template,
               R1Config r1Config,
               PostgresRepository postgresRepository,
               CassandraRepository cassandraRepository) {
        this.template = template;
        this.r1Config = r1Config;
        this.postgresRepository = postgresRepository;
        this.cassandraRepository = cassandraRepository;
    }

    @PostMapping("/start/{name}")
    public ResponseEntity<?> callProcess(@PathVariable("name") String value) {
        try {
            ListNode listNode = new ListNode(value);
            String jsonValue = JsonUtil.toJson(listNode).orElseThrow();
            Map<String, UUID> map = new HashMap<>();
            UUID uuid = UUID.randomUUID();
            map.put("direct:r:1:s:1", uuid);
            BaseModel baseModel = new BaseModel(uuid, "api:camel", "direct:r:1:s:1", jsonValue, map);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody("direct:r:1:s:1", json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), OK);
        }
    }

    @PostMapping("/jump/{sashokId}/{stepFrom}/{stepTo}")
    public ResponseEntity<?> jump(@PathVariable Long sashokId, @PathVariable String stepFrom, @PathVariable String stepTo) {
        try {
            String roadJson = postgresRepository.findRoadById(sashokId).orElseThrow();
            Map<String, UUID> road = JsonUtil.toCollection(roadJson, new TypeReference<Map<String, UUID>>() {}).orElseThrow();
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
    public ResponseEntity<?> retry(@PathVariable Long sashokId, @PathVariable UUID stepId) {
        try {
            String roadJson = postgresRepository.findRoadById(sashokId).orElseThrow();
            StepEntity stepEntity = cassandraRepository.step().findFirstByStepIdAndCreateDateLessThan(stepId, LocalDateTime.now()).orElseThrow();
            Map<String, UUID> road = JsonUtil.toCollection(roadJson, new TypeReference<Map<String, UUID>>() {}).orElseThrow();
            BaseModel baseModel = new BaseModel(stepEntity, road);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(stepEntity.getName(), json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity<>(e.getMessage(), OK);
        }
    }

    @PostMapping("/build/r1")
    public ResponseEntity<?> startR1() throws Exception {
        r1Config.buildRoad();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/cancel/{sashokId}")
    public ResponseEntity<?> cancel(@PathVariable Long sashokId) {
        cassandraRepository.stoppedStep().save(new StoppedStepEntity(sashokId));
        return ResponseEntity.ok().build();
    }

}
