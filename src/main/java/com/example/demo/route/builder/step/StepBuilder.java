package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.Step;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class StepBuilder implements IStepBuilder {

    public Step build(Map<String, Object> value) {
        String name = (String) value.get("name");
        String receiver = (String) value.get("receiver");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new Step(name, receiver, processor, errorHandler, executionTimeToWait);
    }
}
