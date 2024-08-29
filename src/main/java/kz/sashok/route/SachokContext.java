package kz.sashok.route;

import kz.sashok.route.step.AbstractSashokStep;
import org.apache.camel.CamelContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SachokContext {

    private final CamelContext context;

    public SachokContext(CamelContext context) {
        this.context = context;
    }

    public void buildRoute(AbstractSashokStep... steps) throws Exception {
        for (AbstractSashokStep step : steps) {
            context.addRoutes(step);
        }
        context.getRouteController().start();
    }

    public void buildRoute(List<AbstractSashokStep> steps) {
        for (AbstractSashokStep step : steps) {
            try {
                context.addRoutes(step);
            } catch (Exception e) {
                throw new RuntimeException("Add routes");
            }
        }
        context.getRouteController().start();
    }
}
