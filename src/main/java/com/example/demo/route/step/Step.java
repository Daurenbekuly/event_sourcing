package com.example.demo.route.step;

import com.example.demo.repository.SashokRepository;

import static com.example.demo.route.common.Constant.RECEIVER;
import static com.example.demo.route.common.Constant.TIMEOUT;
import static com.example.demo.route.common.KafkaPath.KAFKA_PATH_SASHOK;

public class Step extends SashOkStepBuilder {

    private final String name;
    private final String receiver;
    private final String processor;

    public Step(String name,
                String receiver,
                String processor,
                SashokRepository sashokRepository) {
        super(sashokRepository);
        nameValidator(name);
        nameValidator(receiver);
        this.name = name;
        this.receiver = receiver;
        this.processor = processor;
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
