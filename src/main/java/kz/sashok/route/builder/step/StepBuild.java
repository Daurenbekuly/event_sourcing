package kz.sashok.route.builder.step;

import kz.sashok.route.model.ErrorHandler;
import kz.sashok.route.step.Step;
import org.springframework.stereotype.Service;

import java.util.Map;

import static kz.sashok.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class StepBuild implements StepBuilder {

    public Step build(Map<String, Object> value) {
        String name = (String) value.get("name");
        String receiver = (String) value.get("receiver");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new Step(name, receiver, processor, errorHandler, executionTimeToWait);
    }
}
