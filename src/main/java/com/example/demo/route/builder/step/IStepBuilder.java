package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.AbstractSashokStep;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;
import static com.example.demo.common.JsonUtil.toTypeOrElseThrow;
import static java.util.Objects.isNull;

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
        var errorHandler = value.get("processor");
        if (isNull(errorHandler)) throw new NullPointerException();
        return (String) value.get("processor");
    }

    default ErrorHandler errorHandler(Map<String, Object> value) {
        var errorHandler = value.get("errorHandler");
        if (isNull(errorHandler)) {
            return new ErrorHandler();
        } else {
            return toTypeOrElseThrow(errorHandler, ErrorHandler.class);
        }
    }

    default Long executionTimeToWait(Map<String, Object> value) {
        var executionTimeToWait = value.get("executionTimeToWait");
        if (isNull(executionTimeToWait)) {
            return EXECUTION_TIME_TO_WAIT;
        } else {
            return toTypeOrElseThrow(executionTimeToWait, Long.class);
        }
    }

    private String camelUrl(Object step, String routeName, Integer version) {
        if (isNull(step)) throw new NullPointerException();
        return "direct" + ":r:" + routeName + ":s:" + step + ":v:" + version;
    }
}
