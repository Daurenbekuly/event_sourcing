package com.example.demo.route.config;

import com.example.demo.repository.SashokRepository;
import com.example.demo.route.step.FirstStep;
import com.example.demo.route.step.Step;
import com.example.demo.route.step.LastStep;
import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class R1Config {

    private final ApplicationContext applicationContext;
    private final SashokRepository sashokRepository;

    public R1Config(ApplicationContext applicationContext,
                    SashokRepository sashokRepository) {
        this.applicationContext = applicationContext;
        this.sashokRepository = sashokRepository;
    }

    public void startFirstRoute() throws Exception {
        var s1 = new FirstStep("direct:r:1:s:1", "direct:r:1:s:2", "processor1", sashokRepository);
        var s2 = new Step("direct:r:1:s:2", "direct:r:1:s:3", "processor2", sashokRepository);
//        var s3 = new StepEh("direct:r:1:s:3", "direct:r:1:s:4", "processor3", new ErrorHandler(null, 7, null, null));
        var s3 = new Step("direct:r:1:s:3", "direct:r:1:s:4", "processor3", sashokRepository);
//        var s3 = new StepSp("direct:r:1:s:3", "direct:r:2:s:1", "direct:r:1:s:4", "processor3");
        var s4 = new Step("direct:r:1:s:4", "direct:r:1:s:5", "processor4", sashokRepository);
        var s5 = new LastStep("direct:r:1:s:5", "processor5", sashokRepository);

//        var ss1 = new FirstStep("direct:r:2:s:1", "direct:r:2:s:2", "processor6");
//        var ss2 = new Step("direct:r:2:s:2", "direct:r:2:s:3", "processor7");
//        var ss3 = new Step("direct:r:2:s:3", "direct:r:2:s:4", "processor8");
//        var ss4 = new LastStepBm("direct:r:2:s:s4", "processor9");

        var context = applicationContext.getBean("camelContext", CamelContext.class);
        context.addRoutes(s1);
        context.addRoutes(s2);
        context.addRoutes(s3);
        context.addRoutes(s4);
        context.addRoutes(s5);
//        context.addRoutes(ss1);
//        context.addRoutes(ss2);
//        context.addRoutes(ss3);
//        context.addRoutes(ss4);
        context.getRouteController().start();
    }
}
