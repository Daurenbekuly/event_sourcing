package com.example.demo.route.step;

import com.example.demo.route.step.common.SashOkStepBuilder;
import org.apache.camel.Exchange;

import static com.example.demo.route.common.Constant.BACK_TO_MAIN_ROUTE;
import static com.example.demo.route.common.Constant.KAFKA_PATH;

public class LastStepBm extends SashOkStepBuilder {

    private final String name;
    private final String processor;

    public LastStepBm(String name,
                      String processor) {
        this.name = name;
        this.processor = processor;
    }

    @Override
    public void declareStep() {
        from(name)
                .process(processor)
                .process(BACK_TO_MAIN_ROUTE)
                .choice()
                .when(Exchange::isRouteStop)
                .end()
                .to(KAFKA_PATH)
                .end();
    }
}