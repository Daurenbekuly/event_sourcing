package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.AbstractSashokStep;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

public interface IStepBuilder {

    AbstractSashokStep build(Map<String, Object> value, String routeName, Integer version);

    default String name(Map<String, Object> value, String routeName, Integer version) {
        return camelUrl(value.get("name"), routeName, version);
    }

    default String receiver(Map<String, Object> value, String routeName, Integer version) {
        return camelUrl(value.get("receiver"), routeName, version);
    }

    default String subRouteReceiver(Map<String, Object> value, String routeName, Integer version) {
        return camelUrl(value.get("subRouteReceiver"), routeName, version);
    }

    default String mainRouteReceiver(Map<String, Object> value, String routeName, Integer version) {
        return camelUrl(value.get("mainRouteReceiver"), routeName, version);
    }

    default String processor(Map<String, Object> value) {
        return (String) value.get("processor");
    }

    default ErrorHandler errorHandler(Map<String, Object> value) {
        return (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
    }

    default Long executionTimeToWait(Map<String, Object> value) {
        return (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
    }

    default void validate(Object... o) {
        boolean isAnyNull = Stream.of(o).anyMatch(Objects::isNull);
        if (isAnyNull) throw new IllegalArgumentException();
    }

    private String camelUrl(Object step, String routeName, Integer version) {
        return "direct" + ":r:" + routeName + ":s:" + step + ":v:" + version;
    }
}
