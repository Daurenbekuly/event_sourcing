package com.example.demo.route.step;

import com.example.demo.route.step.common.SashOkStepBuilder;

import static com.example.demo.route.common.Constant.KAFKA_PATH;
import static com.example.demo.route.common.Constant.RECEIVER;

public class Step extends SashOkStepBuilder {

    private final String name;
    private final String receiver;
    private final String processor;

    public Step(String name,
                String receiver,
                String processor) {
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(receiver))
                .process(processor)
                .to(KAFKA_PATH)
                .end();
    }
}
