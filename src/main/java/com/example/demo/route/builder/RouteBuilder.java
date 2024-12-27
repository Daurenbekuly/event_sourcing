package com.example.demo.route.builder;

import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.SachokContext;
import com.example.demo.route.builder.step.IStepBuilder;
import com.example.demo.route.model.BuildRouteData;
import com.example.demo.route.step.AbstractSashokStep;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service
public class RouteBuilder {

    private final SachokContext context;
    private final PostgresRepository postgresRepository;
    private final ApplicationContext applicationContext;

    public RouteBuilder(SachokContext context,
                        PostgresRepository postgresRepository,
                        ApplicationContext applicationContext) {
        this.context = context;
        this.postgresRepository = postgresRepository;
        this.applicationContext = applicationContext;
    }

    public void invoke(BuildRouteData buildRouteData) {
        List<AbstractSashokStep> steps = new ArrayList<>();
        buildRouteData.steps().forEach(buildStep -> {
            AbstractSashokStep step = applicationContext
                    .getBean(buildStep.key(), IStepBuilder.class)
                    .build(buildStep.value());
            steps.add(step);
        });
        context.buildRoute(steps);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void autoBuild() {
        List<String> buildRouteList = postgresRepository.createDataList();
        buildRouteList.forEach(jsonBuildRoute -> {
            BuildRouteData buildRoute = toObjectOrElseThrow(jsonBuildRoute, BuildRouteData.class);
            invoke(buildRoute);
        });
    }
}
