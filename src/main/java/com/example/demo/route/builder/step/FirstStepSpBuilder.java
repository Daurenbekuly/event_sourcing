package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.FirstStepSp;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class FirstStepSpBuilder implements IStepBuilder {

    public FirstStepSp build(Map<String, Object> value, String routeName, Integer version) {
        String name = value.get("name") + ":r:" + routeName + ":v:" + version;
        String subRouteReceiver = value.get("subRouteReceiver") + ":r:" + routeName + ":v:" + version;
        String mainRouteReceiver = value.get("mainRouteReceiver") + ":r:" + routeName + ":v:" + version;
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new FirstStepSp(name, subRouteReceiver, mainRouteReceiver, processor, errorHandler, executionTimeToWait);
    }
}
