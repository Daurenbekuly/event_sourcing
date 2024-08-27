package com.example.demo.route.model;

import static com.example.demo.common.Constant.EXCEPTION_BACKOFF_MULTIPLIER;
import static com.example.demo.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static com.example.demo.common.Constant.MAXIMUM_REDELIVERIES;
import static com.example.demo.common.Constant.REDELIVERY_DELAY;

public record ErrorHandler(
        String exceptionHandler,
        Integer maximumRedeliveries,
        Double exceptionBackOffMultiplier,
        Long redeliveryDelay) {

    public ErrorHandler() {
        this(getExceptionHandler(null),
                getMaximumRedeliveries(null),
                getExceptionBackOffMultiplier(null),
                getRedeliveryDelay(null));
    }

    public ErrorHandler(Integer maximumRedeliveries, Double exceptionBackOffMultiplier, Long redeliveryDelay) {
        this(getExceptionHandler(null),
                getMaximumRedeliveries(maximumRedeliveries),
                getExceptionBackOffMultiplier(exceptionBackOffMultiplier),
                getRedeliveryDelay(redeliveryDelay));
    }

    public ErrorHandler(Integer maximumRedeliveries) {
        this(getExceptionHandler(null),
                getMaximumRedeliveries(maximumRedeliveries),
                getExceptionBackOffMultiplier(null),
                getRedeliveryDelay(null));
    }

    public ErrorHandler(String exceptionHandler, Integer maximumRedeliveries) {
        this(getExceptionHandler(exceptionHandler),
                getMaximumRedeliveries(maximumRedeliveries),
                getExceptionBackOffMultiplier(null),
                getRedeliveryDelay(null));
    }

    private static String getExceptionHandler(String exceptionHandler) {
        return exceptionHandler != null ? exceptionHandler : EXCEPTION_HANDLER_PROCESSOR;
    }

    private static Integer getMaximumRedeliveries(Integer maximumRedeliveries) {
        return maximumRedeliveries != null ? maximumRedeliveries : MAXIMUM_REDELIVERIES;
    }

    private static Double getExceptionBackOffMultiplier(Double exceptionBackOffMultiplier) {
        return exceptionBackOffMultiplier != null ? exceptionBackOffMultiplier : EXCEPTION_BACKOFF_MULTIPLIER;
    }

    private static Long getRedeliveryDelay(Long redeliveryDelay) {
        return redeliveryDelay != null ? redeliveryDelay : REDELIVERY_DELAY;
    }

}
