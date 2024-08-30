package kz.sashok.route.builder.step;

import kz.sashok.route.model.ErrorHandler;
import kz.sashok.route.step.StepSp;
import org.springframework.stereotype.Service;

import java.util.Map;

import static kz.sashok.common.Constant.EXECUTION_TIME_TO_WAIT;

@Service
public class StepSpBuild implements StepBuilder {

    public StepSp build(Map<String, Object> value) {
        String name = (String) value.get("name");
        String subRouteReceiver = (String) value.get("subRouteReceiver");
        String mainRouteReceiver = (String) value.get("mainRouteReceiver");
        String processor = (String) value.get("processor");
        ErrorHandler errorHandler = (ErrorHandler) value.getOrDefault("errorHandler", new ErrorHandler());
        Long executionTimeToWait = (Long) value.getOrDefault("executionTimeToWait", EXECUTION_TIME_TO_WAIT);
        return new StepSp(name, subRouteReceiver, mainRouteReceiver, processor, errorHandler, executionTimeToWait);
    }
}
