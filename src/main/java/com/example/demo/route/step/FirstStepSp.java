package com.example.demo.route.step;

import com.example.demo.route.model.ErrorHandler;

import static com.example.demo.common.Constant.INIT_SUB_ROUTE_PROCESSOR;
import static com.example.demo.common.Header.MAIN_ROUTE_RECEIVER;
import static com.example.demo.common.Header.RECEIVER;
import static com.example.demo.common.Header.TIMEOUT;
import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;

public class FirstStepSp extends AbstractSashokStep {

    private final String name;
    private final String subRouteReceiver;
    private final String mainRouteReceiver;
    private final String processor;

    public FirstStepSp(String name,
                       String subRouteReceiver,
                       String mainRouteReceiver,
                       String processor,
                       ErrorHandler errorHandler,
                       Long executionTimeToWait) {
        nameValidator(name);
        nameValidator(subRouteReceiver);
        nameValidator(mainRouteReceiver);
        this.name = name;
        this.subRouteReceiver = subRouteReceiver;
        this.mainRouteReceiver = mainRouteReceiver;
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
                .setHeader(RECEIVER, constant(subRouteReceiver))
                .setHeader(TIMEOUT, constant(executionTime))
                .process(processor)
                .setHeader(MAIN_ROUTE_RECEIVER, constant(mainRouteReceiver))
                .process(INIT_SUB_ROUTE_PROCESSOR)
                .to(KAFKA_PATH_SASHOK)
                .end();
    }
}
