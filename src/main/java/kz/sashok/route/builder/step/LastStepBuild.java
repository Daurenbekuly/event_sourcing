package kz.sashok.route.builder.step;

import kz.sashok.route.model.ErrorHandler;
import kz.sashok.route.step.LastStep;
import org.springframework.stereotype.Service;

import java.util.Map;

import static kz.sashok.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class LastStepBuild implements StepBuilder {

    public LastStep build(Map<String, Object> value) {
        String name = (String) value.get("name");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new LastStep(name, processor, errorHandler, executionTimeToWait);
    }
}
