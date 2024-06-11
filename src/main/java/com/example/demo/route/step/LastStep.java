package com.example.demo.route.step;

import static com.example.demo.route.common.Constant.LAST_STEP_PROCESSOR;
import static com.example.demo.route.common.Constant.TIMEOUT;

public class LastStep extends AbstractSashOkStepBuilder {

    private final String name;
    private final String processor;

    public LastStep(String name,
                    String processor) {
        nameValidator(name);
        this.name = name;
        this.processor = processor;
    }

    @Override
    public void declareStep() {
        from(name)
                .setHeader(TIMEOUT, constant(5000L))
                .process(processor)
                .process(LAST_STEP_PROCESSOR)
                .end();
    }
}
