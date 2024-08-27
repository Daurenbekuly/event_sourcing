package com.example.demo.route;

import com.example.demo.route.step.AbstractSashokStep;
import org.apache.camel.CamelContext;
import org.springframework.stereotype.Component;

@Component
public class SachokContext {

    private final CamelContext context;

    public SachokContext(CamelContext context) {
        this.context = context;
    }

    public void buildRoad(AbstractSashokStep... stepBuilders) throws Exception {
        for (AbstractSashokStep stepBuilder : stepBuilders) {
            context.addRoutes(stepBuilder);
        }
        context.getRouteController().start();
    }
}
