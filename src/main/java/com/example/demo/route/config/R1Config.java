package com.example.demo.route.config;

import com.example.demo.model.ErrorHandler;
import com.example.demo.route.step.FirstStep;
import com.example.demo.route.step.LastStepBm;
import com.example.demo.route.step.Step;
import com.example.demo.route.step.LastStep;
import com.example.demo.route.step.StepEh;
import com.example.demo.route.step.StepSp;
import org.apache.camel.CamelContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class R1Config {

    private final ApplicationContext applicationContext;

    public R1Config(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    public void startFirstRoute() throws Exception {
        var s1 = new FirstStep("direct:r1:s1", "direct:r1:s2", "processor1");
        var s2 = new Step("direct:r1:s2", "direct:r1:s3", "processor2");
//        var s3 = new StepEh("direct:r1:s3", "direct:r1:s4", "processor3", new ErrorHandler(null, 7, null, null));
        var s3 = new StepSp("direct:r1:s3", "direct:r1:ss1", "direct:r1:s4", "processor3");
        var s4 = new Step("direct:r1:s4", "direct:r1:s5", "processor4");
        var s5 = new LastStep("direct:r1:s5", "processor5");

        var ss1 = new FirstStep("direct:r1:ss1", "direct:r1:ss2", "processor6");
        var ss2 = new Step("direct:r1:ss2", "direct:r1:ss3", "processor7");
        var ss3 = new Step("direct:r1:ss3", "direct:r1:ss4", "processor8");
        var ss4 = new LastStepBm("direct:r1:ss4", "processor9");

        var context = applicationContext.getBean("camelContext", CamelContext.class);
        context.addRoutes(s1);
        context.addRoutes(s2);
        context.addRoutes(s3);
        context.addRoutes(s4);
        context.addRoutes(s5);
        context.addRoutes(ss1);
        context.addRoutes(ss2);
        context.addRoutes(ss3);
        context.addRoutes(ss4);
        context.getRouteController().start();
    }
}
