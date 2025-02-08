package com.example.demo.route.builder.step;

import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.FirstStepSp;
import org.springframework.stereotype.Service;

import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class FirstStepSpBuilder implements IStepBuilder {

    public FirstStepSp build(Map<String, Object> value, String routeName, Integer version) {
        String name = name(value, routeName, version);
        String subRouteReceiver = subRouteReceiver(value, routeName, version);
        String mainRouteReceiver = mainRouteReceiver(value, routeName, version);
        String processor = processor(value);
        ErrorHandler errorHandler = errorHandler(value);
        Long executionTimeToWait = executionTimeToWait(value);
        validate(name, subRouteReceiver, mainRouteReceiver, processor, errorHandler, executionTimeToWait);
        return new FirstStepSp(name, subRouteReceiver, mainRouteReceiver, processor, errorHandler, executionTimeToWait);
    }
}
