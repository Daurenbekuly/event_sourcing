package com.example.demo.route.step;

import com.example.demo.route.step.common.SashOkStepBuilder;

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
                .end();
    }
}
