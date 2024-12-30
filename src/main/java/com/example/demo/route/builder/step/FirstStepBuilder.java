package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.FirstStep;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class FirstStepBuilder implements IStepBuilder {

    public FirstStep build(Map<String, Object> value, String routeName, Integer version) {
        String name = "direct" + ":r:" + routeName + ":s:" + value.get("name") + ":v:" + version;
        String receiver = "direct" + ":r:" + routeName + ":s:" + value.get("receiver") + ":v:" + version;
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new FirstStep(name, receiver, processor, errorHandler, executionTimeToWait);
    }
}
