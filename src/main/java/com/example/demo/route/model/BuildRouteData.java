package com.example.demo.route.model;

import java.util.List;
import java.util.Map;

public record BuildRouteData(String name, List<BuildStepData> steps) {

    public record BuildStepData(String key, Map<String, Object> value) {
    }
}

