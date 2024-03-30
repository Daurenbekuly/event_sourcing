package com.example.demo.route.step;

import static com.example.demo.route.common.Constant.LAST_STEP_PROCESSOR;

public class LastStep extends SashOkStepBuilder {

    private final String name;
    private final String processor;

    public LastStep(String name,
                    String processor) {
        this.name = name;
        this.processor = processor;
    }

    @Override
    public void declareStep() {
        from(name)
                .process(processor)
                .process(LAST_STEP_PROCESSOR)
                .end();
    }
}
