package com.example.demo.route.model;

import com.example.demo.route.builder.Components;

import java.util.List;
import java.util.Map;

public record BuildRouteData(String name, List<BuildStepData> steps) {

    public record BuildStepData(Components.Steps key, Map<String, Object> value) {
    }
}

