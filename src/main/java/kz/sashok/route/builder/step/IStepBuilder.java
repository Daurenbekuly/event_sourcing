package kz.sashok.route.builder.step;

import kz.sashok.route.step.AbstractSashokStep;

import java.util.Map;

public interface IStepBuilder {

    AbstractSashokStep build(Map<String, Object> value);
}
