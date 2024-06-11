package com.example.demo.route.step;

import org.apache.camel.Exchange;

import static com.example.demo.common.Constant.BACK_TO_MAIN_ROUTE_PROCESSOR;
import static com.example.demo.common.Constant.LAST_STEP_PROCESSOR;
import static com.example.demo.common.Constant.TIMEOUT;
import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;

public class LastStepBm extends AbstractSashOkStepBuilder {

    private final String name;
    private final String processor;

    public LastStepBm(String name,
                      String processor) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(TIMEOUT, constant(executionTimeToWait))
                .process(processor)
                .process(BACK_TO_MAIN_ROUTE_PROCESSOR)
                .choice()
                .when(Exchange::isRouteStop)
                .process(LAST_STEP_PROCESSOR)
                .end()
                .to(KAFKA_PATH_SASHOK)
                .end();
    }
}