package com.example.demo.route.step;

import com.example.demo.model.ErrorHandler;
import com.example.demo.route.step.common.SashOkStepBuilder;

import static com.example.demo.route.common.Constant.KAFKA_PATH;
import static com.example.demo.route.common.Constant.RECEIVER;

public class StepEh extends SashOkStepBuilder {

    private final String name;
    private final String receiver;
    private final String processor;

    public StepEh(String name,
                  String receiver,
                  String processor,
                  ErrorHandler errorHandler) {
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
        this.redeliveryDelay = errorHandler.redeliveryDelay();
        this.exceptionHandler = errorHandler.exceptionHandler();
        this.maximumRedeliveries = errorHandler.maximumRedeliveries();
        this.exceptionBackOffMultiplier = errorHandler.exceptionBackOffMultiplier();
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(receiver))
                .process(processor)
                .to(KAFKA_PATH)
                .end();
    }


}
