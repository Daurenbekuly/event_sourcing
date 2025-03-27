package com.example.demo.route.step;

import com.example.demo.route.model.ErrorHandler;

import static com.example.demo.common.Constant.FIRST_STEP_PROCESSOR;
import static com.example.demo.common.Header.RECEIVER;
import static com.example.demo.common.Header.TIMEOUT;
import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;

public class FirstStep extends AbstractSashokStep {

    private final String name;
    private final String receiver;
    private final String processor;

    public FirstStep(String name,
                     String receiver,
                     String processor,
                     ErrorHandler errorHandler,
                     Long executionTimeToWait) {
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
        this.redeliveryDelay = errorHandler.redeliveryDelay();
        this.exceptionHandler = errorHandler.exceptionHandler();
        this.maximumRedeliveries = errorHandler.maximumRedeliveries();
        this.backOffMultiplier = errorHandler.exceptionBackOffMultiplier();
        this.executionTime = executionTimeToWait;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(receiver))
                .setHeader(TIMEOUT, constant(executionTime))
                .process(FIRST_STEP_PROCESSOR)
                .process(processor)
                .to(KAFKA_PATH_SASHOK)
                .end();
    }
}
