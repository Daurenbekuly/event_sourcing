package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.LastStep;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class LastStepBuilder implements IStepBuilder {

    public LastStep build(Map<String, Object> value, String routeName, Integer version) {
        String name = name(value, routeName, version);
        String processor = processor(value);
        ErrorHandler errorHandler = errorHandler(value);
        Long executionTimeToWait = executionTimeToWait(value);
        validate(name, processor, errorHandler, executionTimeToWait);
        return new LastStep(name, processor, errorHandler, executionTimeToWait);
    }
}
