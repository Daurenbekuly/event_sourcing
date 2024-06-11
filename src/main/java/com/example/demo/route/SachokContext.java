package com.example.demo.route;

import com.example.demo.route.step.AbstractSashOkStepBuilder;
import org.apache.camel.CamelContext;
import org.springframework.stereotype.Component;

@Component
public class SachokContext {

    private final CamelContext context;

    public SachokContext(CamelContext context) {
        this.context = context;
    }

    public void buildRoad(AbstractSashOkStepBuilder... stepBuilders) throws Exception {
        for (AbstractSashOkStepBuilder stepBuilder : stepBuilders) {
            context.addRoutes(stepBuilder);
        }
        context.getRouteController().start();
    }
}
