package kz.sashok.route.builder.step;

import kz.sashok.route.step.AbstractSashokStep;

import java.util.Map;

public interface StepBuilder {

    AbstractSashokStep build(Map<String, Object> value);
}
