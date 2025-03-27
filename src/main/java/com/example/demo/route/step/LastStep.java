package com.example.demo.route.step;

import com.example.demo.route.model.ErrorHandler;

import static com.example.demo.common.Constant.LAST_STEP_PROCESSOR;
import static com.example.demo.common.Header.TIMEOUT;

public class LastStep extends AbstractSashokStep {

    private final String name;
    private final String processor;

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
        this.backOffMultiplier = errorHandler.exceptionBackOffMultiplier();
        this.executionTime = executionTimeToWait;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(TIMEOUT, constant(executionTime))
                .process(processor)
                .process(LAST_STEP_PROCESSOR)
                .end();
    }
}
