package com.example.demo.route.model;

import static com.example.demo.common.Constant.*;

public record ErrorHandler(
        String exceptionHandler,
        Integer maximumRedeliveries,
        Double exceptionBackOffMultiplier,
        Long redeliveryDelay) {

    public ErrorHandler() {
        this(EXCEPTION_HANDLER_PROCESSOR,
                MAX_REDELIVERY,
                BACKOFF_MULTIPLIER,
                REDELIVERY_DELAY);
    }
}
