package com.example.demo.route.step;

import com.example.demo.route.step.common.SashOkStepBuilder;

import static com.example.demo.route.common.Constant.MAIN_ROUTE_RECEIVER;
import static com.example.demo.route.common.Constant.INIT_SUB_ROUTE_PROCESSOR;
import static com.example.demo.route.common.Constant.KAFKA_PATH;
import static com.example.demo.route.common.Constant.RECEIVER;

public class StepSp extends SashOkStepBuilder {

    private final String name;
    private final String subRouteReceiver;
    private final String mainRouteReceiver;
    private final String processor;

    public StepSp(String name,
                  String subRouteReceiver,
                  String mainRouteReceiver,
                  String processor) {
        this.name = name;
        this.subRouteReceiver = subRouteReceiver;
        this.mainRouteReceiver = mainRouteReceiver;
        this.processor = processor;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(subRouteReceiver))
                .process(processor)
                .setHeader(MAIN_ROUTE_RECEIVER, constant(mainRouteReceiver))
                .process(INIT_SUB_ROUTE_PROCESSOR)
                .to(KAFKA_PATH)
                .end();
    }
}
