package com.example.demo.route.builder.step;

import com.example.demo.route.step.AbstractSashokStep;

import java.util.Map;

public interface IStepBuilder {

    AbstractSashokStep build(Map<String, Object> value);
}
