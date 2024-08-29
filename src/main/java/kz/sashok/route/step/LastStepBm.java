package kz.sashok.route.step;

import kz.sashok.route.model.ErrorHandler;
import org.apache.camel.Exchange;

import static kz.sashok.common.Constant.BACK_TO_MAIN_ROUTE_PROCESSOR;
import static kz.sashok.common.Constant.LAST_STEP_PROCESSOR;
import static kz.sashok.common.Constant.TIMEOUT;
import static kz.sashok.common.KafkaPath.KAFKA_PATH_SASHOK;

public class LastStepBm extends AbstractSashokStep {

    private final String name;
    private final String processor;

    public LastStepBm(String name,
                      String processor) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
    }

    public LastStepBm(String name,
                      String processor,
                      Long executionTimeToWait) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
        this.executionTimeToWait = executionTimeToWait;
    }

    public LastStepBm(String name,
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

    public LastStepBm(String name,
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
                .process(BACK_TO_MAIN_ROUTE_PROCESSOR)
                .choice()
                    .when(Exchange::isRouteStop)
                        .process(LAST_STEP_PROCESSOR)
                    .otherwise()
                        .to(KAFKA_PATH_SASHOK)
                .end();
    }
}