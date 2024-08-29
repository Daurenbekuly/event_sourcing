package kz.sashok.route.step;

import kz.sashok.route.model.ErrorHandler;

import static kz.sashok.common.Constant.BEFORE_USER_TASK_PROCESSOR;
import static kz.sashok.common.Constant.RECEIVER;
import static kz.sashok.common.Constant.TIMEOUT;

public class StepBeforeUt extends AbstractSashokStep {

    private final String name;
    private final String receiver;
    private final String processor;

    public StepBeforeUt(String name,
                        String receiver,
                        String processor) {
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
    }

    public StepBeforeUt(String name,
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

    public StepBeforeUt(String name,
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

    public StepBeforeUt(String name,
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
                .process(processor)
                .process(BEFORE_USER_TASK_PROCESSOR)
                .end();
    }
}
