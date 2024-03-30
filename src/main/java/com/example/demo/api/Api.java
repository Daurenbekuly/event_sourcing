package com.example.demo.api;

import com.example.demo.model.BaseModel;
import com.example.demo.route.config.R1Config;
import com.example.demo.util.JsonUtil;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.route.common.Constant.KAFKA_PATH;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/camel")
public class Api {

    private final ProducerTemplate template;
    private final R1Config r1Config;

    public Api(ProducerTemplate template, R1Config r1Config) {
        this.template = template;
        this.r1Config = r1Config;
    }

    @PostMapping("/{name}")
    public ResponseEntity<?> callProcess(@PathVariable String name) {
        try {
            Map<String, UUID> map = new HashMap<>();
            UUID uuid = UUID.randomUUID();
            map.put("direct:r1:s1", uuid);
            BaseModel baseModel = new BaseModel(uuid, "api:camel", "direct:r1:s1", name, map);
            String json = JsonUtil.toJson(baseModel).orElseThrow();
            template.asyncRequestBody(KAFKA_PATH, json);
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            System.out.println("XDD");
            return new ResponseEntity<>(e.getCause().getMessage(), OK);
        }
    }

    @PostMapping("/start/r1")
    public ResponseEntity<?> startR1() throws Exception {
        r1Config.startFirstRoute();
        return ResponseEntity.ok().build();
    }

}
