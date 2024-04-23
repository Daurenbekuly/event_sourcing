package com.example.demo.route.step;

import com.example.demo.model.ErrorHandler;
import com.example.demo.repository.SashokRepository;

import static com.example.demo.route.common.Constant.RECEIVER;
import static com.example.demo.route.common.Constant.TIMEOUT;
import static com.example.demo.route.common.KafkaPath.KAFKA_PATH_SASHOK;

public class StepEh extends SashOkStepBuilder {

    private final String name;
    private final String receiver;
    private final String processor;

    public StepEh(String name,
                  String receiver,
                  String processor,
                  ErrorHandler errorHandler,
                  SashokRepository sashokRepository) {
        super(sashokRepository);
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

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(receiver))
                .setHeader(TIMEOUT, constant(5000L))
                .process(processor)
                .to(KAFKA_PATH_SASHOK)
                .end();
    }


}
