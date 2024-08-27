package com.example.demo.route.step;

import com.example.demo.route.model.ErrorHandler;

import static com.example.demo.common.Constant.FIRST_STEP_PROCESSOR;
import static com.example.demo.common.Constant.RECEIVER;
import static com.example.demo.common.Constant.TIMEOUT;
import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;

public class FirstStep extends AbstractSashokStep {

    private final String name;
    private final String receiver;
    private final String processor;

    public FirstStep(String name,
                     String receiver,
                     String processor) {
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
    }

    public FirstStep(String name,
                     String receiver,
                     String processor,
                     Long executionTimeToWait) {
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
        this.executionTimeToWait = executionTimeToWait;
    }

    public FirstStep(String name,
                     String receiver,
                     String processor,
                     ErrorHandler errorHandler) {
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
        this.redeliveryDelay = errorHandler.redeliveryDelay();
        this.exceptionHandler = errorHandler.exceptionHandler();
        this.maximumRedeliveries = errorHandler.maximumRedeliveries();
        this.exceptionBackOffMultiplier = errorHandler.exceptionBackOffMultiplier();
    }

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
        this.exceptionBackOffMultiplier = errorHandler.exceptionBackOffMultiplier();
        this.executionTimeToWait = executionTimeToWait;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(receiver))
                .setHeader(TIMEOUT, constant(executionTimeToWait))
                .process(FIRST_STEP_PROCESSOR)
                .process(processor)
                .to(KAFKA_PATH_SASHOK)
                .end();
    }
}
