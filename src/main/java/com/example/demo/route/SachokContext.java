package com.example.demo.route;

import com.example.demo.route.step.AbstractSashokStep;
import org.apache.camel.CamelContext;
import org.apache.camel.Route;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SachokContext {

    private final CamelContext context;

    public SachokContext(CamelContext context) {
        this.context = context;
    }

    public void buildRoad(AbstractSashokStep... steps) throws Exception {
        for (AbstractSashokStep step : steps) {
            context.addRoutes(step);
        }
        context.getRouteController().start();
    }

    public void buildRoad(List<AbstractSashokStep> steps) throws Exception {
        for (AbstractSashokStep step : steps) {
            context.addRoutes(step);
        }
        context.getRouteController().start();
    }
}
