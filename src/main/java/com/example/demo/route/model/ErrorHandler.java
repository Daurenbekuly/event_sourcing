package com.example.demo.route.model;

import static com.example.demo.common.Constant.DEFAULT_EXCEPTION_BACK_OFF_MULTI;
import static com.example.demo.common.Constant.DEFAULT_MAX_REDELIVERIES;
import static com.example.demo.common.Constant.DEFAULT_REDELIVERY_DELAY;

public record ErrorHandler(
        String exceptionHandler,
        Integer maximumRedeliveries,
        Double exceptionBackOffMultiplier,
        Long redeliveryDelay) {

    public ErrorHandler(String exceptionHandler) {
        this(exceptionHandler, DEFAULT_MAX_REDELIVERIES, DEFAULT_EXCEPTION_BACK_OFF_MULTI, DEFAULT_REDELIVERY_DELAY);
    }

    public ErrorHandler(String exceptionHandler, Integer maximumRedeliveries) {
        this(exceptionHandler, maximumRedeliveries, DEFAULT_EXCEPTION_BACK_OFF_MULTI, DEFAULT_REDELIVERY_DELAY);
    }
}
