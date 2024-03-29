package com.example.demo.model;

import java.time.Instant;
import java.util.Map;
import java.util.Queue;

public record Step(
        String name,
        String receiverName,
        String jsonValue,
        Instant statDate,
        Integer retryCount,
        Map<String, String> road,
        Queue<String> correlations) {

}
