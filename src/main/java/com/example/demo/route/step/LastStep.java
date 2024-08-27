package com.example.demo.route.step;

import com.example.demo.route.model.ErrorHandler;

import static com.example.demo.common.Constant.LAST_STEP_PROCESSOR;
import static com.example.demo.common.Constant.TIMEOUT;

public class LastStep extends AbstractSashokStep {

    private final String name;
    private final String processor;

    public LastStep(String name,
                    String processor) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
    }

    public LastStep(String name,
                    String processor,
                    Long executionTimeToWait) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
        this.executionTimeToWait = executionTimeToWait;
    }

    public LastStep(String name,
                    String processor,
                    ErrorHandler errorHandler) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
        this.redeliveryDelay = errorHandler.redeliveryDelay();
        this.exceptionHandler = errorHandler.exceptionHandler();
        this.maximumRedeliveries = errorHandler.maximumRedeliveries();
        this.exceptionBackOffMultiplier = errorHandler.exceptionBackOffMultiplier();
    }

    public LastStep(String name,
                    String processor,
                    ErrorHandler errorHandler,
                    Long executionTimeToWait) {
        nameValidator(name);
        this.name = name;
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
                .setHeader(TIMEOUT, constant(executionTimeToWait))
                .process(processor)
                .process(LAST_STEP_PROCESSOR)
                .end();
    }
}
