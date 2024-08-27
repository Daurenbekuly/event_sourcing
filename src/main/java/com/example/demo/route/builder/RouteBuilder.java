package com.example.demo.route.builder;

import com.example.demo.route.SachokContext;
import com.example.demo.route.model.BuildRouteList;
import com.example.demo.route.model.ErrorHandler;
import com.example.demo.route.step.AbstractSashokStep;
import com.example.demo.route.step.FirstStep;
import com.example.demo.route.step.LastStep;
import com.example.demo.route.step.LastStepBm;
import com.example.demo.route.step.Step;
import com.example.demo.route.step.StepSp;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class RouteBuilder {

    private final SachokContext context;

    public RouteBuilder(SachokContext context) {
        this.context = context;
    }

    public void invoke(BuildRouteList buildRouteList) throws Exception {
        List<AbstractSashokStep> steps = new ArrayList<>();

        buildRouteList.steps().forEach(step -> {
            var sashokStep = switch (step.key()) {
                case FIRST_STEP -> buildFirstStep(step.value());
                case STEP -> buildStep(step.value());
                case STEP_SP -> buildStepSp(step.value());
                case LAST_STEP_BM -> buildLastStepBm(step.value());
                case LAST_STEP -> buildLastStep(step.value());
            };
            steps.add(sashokStep);
        });
        context.buildRoad(steps);
    }

    private FirstStep buildFirstStep(Map<String, Object> value) {
        String name = (String) value.get("name");
        String receiver = (String) value.get("receiver");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new FirstStep(name, receiver, processor, errorHandler, executionTimeToWait);
    }

    private Step buildStep(Map<String, Object> value) {
        String name = (String) value.get("name");
        String receiver = (String) value.get("receiver");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new Step(name, receiver, processor, errorHandler, executionTimeToWait);
    }

    private StepSp buildStepSp(Map<String, Object> value) {
        String name = (String) value.get("name");
        String subRouteReceiver = (String) value.get("subRouteReceiver");
        String mainRouteReceiver = (String) value.get("mainRouteReceiver");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new StepSp(name, subRouteReceiver, mainRouteReceiver, processor, errorHandler, executionTimeToWait);
    }

    private LastStepBm buildLastStepBm(Map<String, Object> value) {
        String name = (String) value.get("name");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new LastStepBm(name, processor, errorHandler, executionTimeToWait);
    }

    private LastStep buildLastStep(Map<String, Object> value) {
        String name = (String) value.get("name");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new LastStep(name, processor, errorHandler, executionTimeToWait);
    }
}
