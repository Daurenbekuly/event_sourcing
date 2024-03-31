package com.example.demo.route.step;

import static com.example.demo.route.common.Constant.RECEIVER;

public class StepKh extends SashOkStepBuilder {

    private final String name;
    private final String receiver;
    private final String processor;

    public StepKh(String name,
                  String receiver,
                  String processor,
                  String kafkaRetryPath) {
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
        this.kafkaRetryPath = kafkaRetryPath;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(RECEIVER, constant(receiver))
                .process(processor)
                .to(kafkaRetryPath)
                .end();
    }
}
