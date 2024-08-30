package kz.sashok.route.builder;

import kz.sashok.repository.postgres.PostgresRepository;
import kz.sashok.route.SachokContext;
import kz.sashok.route.builder.step.IStepBuilder;
import kz.sashok.route.model.BuildRouteData;
import kz.sashok.route.step.AbstractSashokStep;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static kz.sashok.common.JsonUtil.toObject;

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
            var sashokStep = applicationContext
                    .getBean(buildStep.key(), IStepBuilder.class)
                    .build(buildStep.value());
            steps.add(sashokStep);
        });
        context.buildRoute(steps);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void autoBuild() {
        List<String> buildRouteList = postgresRepository.createDataList();
        buildRouteList.forEach(jsonBuildRoute -> {
            BuildRouteData buildRoute = toObject(jsonBuildRoute, BuildRouteData.class).orElseThrow();
            invoke(buildRoute);
        });
    }
}
