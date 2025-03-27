package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.FirstStepSp;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class FirstStepSpBuilder implements IStepBuilder {

    public FirstStepSp build(Map<String, Object> value, String routeName, Integer version) {
        String name = name(value, routeName, version);
        String subRouteReceiver = subRouteReceiver(value, routeName, version);
        String mainRouteReceiver = mainRouteReceiver(value, routeName, version);
        String processor = processor(value);
        ErrorHandler errorHandler = errorHandler(value);
        Long executionTimeToWait = executionTimeToWait(value);
        return new FirstStepSp(name, subRouteReceiver, mainRouteReceiver, processor, errorHandler, executionTimeToWait);
    }
}
