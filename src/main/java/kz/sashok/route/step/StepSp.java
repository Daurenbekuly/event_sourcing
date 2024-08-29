package kz.sashok.route.step;

import kz.sashok.route.model.ErrorHandler;

import static kz.sashok.common.Constant.MAIN_ROUTE_RECEIVER;
import static kz.sashok.common.Constant.INIT_SUB_ROUTE_PROCESSOR;
import static kz.sashok.common.Constant.RECEIVER;
import static kz.sashok.common.Constant.TIMEOUT;
import static kz.sashok.common.KafkaPath.KAFKA_PATH_SASHOK;

public class StepSp extends AbstractSashokStep {

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

    public StepSp(String name,
                  String subRouteReceiver,
                  String mainRouteReceiver,
                  String processor,
                  ErrorHandler errorHandler) {
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
        this.exceptionBackOffMultiplier = errorHandler.exceptionBackOffMultiplier();
    }

    public StepSp(String name,
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
        this.exceptionBackOffMultiplier = errorHandler.exceptionBackOffMultiplier();
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
