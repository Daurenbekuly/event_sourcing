package com.example.demo.route.model;

import com.example.demo.route.builder.Components;

import java.util.List;
import java.util.Map;

public record BuildRouteList(List<BuildRouteData> steps) {

    public record BuildRouteData(Components.Steps key, Map<String, Object> value) {
    }
}

