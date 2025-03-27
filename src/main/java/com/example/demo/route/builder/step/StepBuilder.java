package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.Step;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class StepBuilder implements IStepBuilder {

    public Step build(Map<String, Object> value, String routeName, Integer version) {
        String name = name(value, routeName, version);
        String receiver = receiver(value, routeName, version);
        String processor = processor(value);
        ErrorHandler errorHandler = errorHandler(value);
        Long executionTimeToWait = executionTimeToWait(value);
        return new Step(name, receiver, processor, errorHandler, executionTimeToWait);
    }
}
