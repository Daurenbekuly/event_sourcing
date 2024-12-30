package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.StepBeforeUt;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class StepBeforeUtBuilder implements IStepBuilder {

    public StepBeforeUt build(Map<String, Object> value, String routeName, Integer version) {
        String name = value.get("name") + ":r:" + routeName + ":v:" + version;
        String receiver = value.get("receiver") + ":r:" + routeName + ":v:" + version;
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new StepBeforeUt(name, receiver, processor, errorHandler, executionTimeToWait);
    }
}
