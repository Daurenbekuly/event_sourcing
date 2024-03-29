package com.example.demo.api;

import com.example.demo.model.Step;
import com.example.demo.util.JsonUtil;
import org.apache.camel.ProducerTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/camel")
public class Api {

    private final ProducerTemplate template;

    public Api(ProducerTemplate template) {
        this.template = template;
    }

    @PostMapping("/{name}")
    public ResponseEntity<?> callProcess(@PathVariable String name) {
        try {
            Step step = new Step("api:camel", "direct:routeFirst:s1", name, Instant.now(), 5, new LinkedHashMap<>(), new LinkedList<>());
            Optional<String> json = JsonUtil.toJson(step);
            template.asyncRequestBody("kafka:es-topic?brokers=localhost:29092", json.get());
            return new ResponseEntity<>(OK);
        } catch (Exception e) {
            System.out.println("XDD");
            return new ResponseEntity<>(e.getCause().getMessage(), OK);
        }
    }

}
