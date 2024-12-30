package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.LastStepSp;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class LastStepBmBuilder implements IStepBuilder {

    public LastStepSp build(Map<String, Object> value, String routeName, Integer version) {
        String name = "direct" + ":r:" + routeName + ":s:" + value.get("name") + ":v:" + version;
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new LastStepSp(name, processor, errorHandler, executionTimeToWait);
    }
}
