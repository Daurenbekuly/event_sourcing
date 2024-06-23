package com.example.demo.route.step;

import static com.example.demo.common.Constant.MAIN_ROUTE_RECEIVER;
import static com.example.demo.common.Constant.INIT_SUB_ROUTE_PROCESSOR;
import static com.example.demo.common.Constant.RECEIVER;
import static com.example.demo.common.Constant.TIMEOUT;
import static com.example.demo.common.KafkaPath.KAFKA_PATH_SASHOK;

public class StepSp extends AbstractSashOkStepBuilder {

    private final String name;
    private final String subRouteReceiver;
    private final String mainRouteReceiver;
    private final String processor;

    public StepSp(String name,
                  String subRouteReceiver,
                  String mainRouteReceiver,
                  String processor) {
        nameValidator(name);
        nameValidator(subRouteReceiver);
        nameValidator(mainRouteReceiver);
        this.name = name;
        this.subRouteReceiver = subRouteReceiver;
        this.mainRouteReceiver = mainRouteReceiver;
        this.processor = processor;
    }

    public StepSp(String name,
                  String subRouteReceiver,
                  String mainRouteReceiver,
                  String processor,
                  Long executionTimeToWait) {
        nameValidator(name);
        nameValidator(subRouteReceiver);
        nameValidator(mainRouteReceiver);
        this.name = name;
        this.subRouteReceiver = subRouteReceiver;
        this.mainRouteReceiver = mainRouteReceiver;
        this.processor = processor;
        this.executionTimeToWait = executionTimeToWait;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(subRouteReceiver))
                .setHeader(TIMEOUT, constant(executionTimeToWait))
                .process(processor)
                .setHeader(MAIN_ROUTE_RECEIVER, constant(mainRouteReceiver))
                .process(INIT_SUB_ROUTE_PROCESSOR)
                .to(KAFKA_PATH_SASHOK)
                .end();
    }
}
