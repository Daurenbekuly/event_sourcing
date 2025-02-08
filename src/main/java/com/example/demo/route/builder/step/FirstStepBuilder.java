package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.FirstStep;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FirstStepBuilder implements IStepBuilder {

    public FirstStep build(Map<String, Object> value, String routeName, Integer version) {
        String name = name(value, routeName, version);
        String receiver = receiver(value, routeName, version);
        String processor = processor(value);
        ErrorHandler errorHandler = errorHandler(value);
        Long executionTimeToWait = executionTimeToWait(value);
        validate(name, receiver, processor, errorHandler, executionTimeToWait);
        return new FirstStep(name, receiver, processor, errorHandler, executionTimeToWait);
    }
}
