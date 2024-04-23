package com.example.demo.model;

public record ErrorHandler(
        String exceptionHandler,
        Integer maximumRedeliveries,
        Double exceptionBackOffMultiplier,
        Long redeliveryDelay) {

    public ErrorHandler(String exceptionHandler, Integer maximumRedeliveries, Double exceptionBackOffMultiplier, Long redeliveryDelay) {
        this.exceptionHandler = exceptionHandler != null ? exceptionHandler : "direct:defErrorHandler";
        this.maximumRedeliveries = maximumRedeliveries != null ? maximumRedeliveries : 5;
        this.exceptionBackOffMultiplier = exceptionBackOffMultiplier != null ? exceptionBackOffMultiplier : 2.0;
        this.redeliveryDelay = redeliveryDelay != null ? redeliveryDelay : 5000;
    }
}
