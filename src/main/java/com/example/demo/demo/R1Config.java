package com.example.demo.demo;

import com.example.demo.route.SachokContext;
import com.example.demo.route.step.FirstStep;
import com.example.demo.route.step.LastStep;
import com.example.demo.route.step.Step;
import org.springframework.stereotype.Service;

@Service
public class R1Config {

    private final SachokContext context;

    public R1Config(SachokContext context) {
        this.context = context;
    }

    public void buildRoad() throws Exception {
        var s1 = new FirstStep("direct:r:1:s:1", "direct:r:1:s:2", "processor1");
        var s2 = new Step("direct:r:1:s:2", "direct:r:1:s:3", "processor2");
        var s3 = new Step("direct:r:1:s:3", "direct:r:1:s:4", "processor3");
        var s4 = new Step("direct:r:1:s:4", "direct:r:1:s:5", "processor4");
        var s5 = new LastStep("direct:r:1:s:5", "processor5");

//        var s3 = new StepEh("direct:r:1:s:3", "direct:r:1:s:4", "processor3", new ErrorHandler(null, 7, null, null));
//        var s3 = new StepSp("direct:r:1:s:3", "direct:r:2:s:1", "direct:r:1:s:4", "processor3");
//        var ss1 = new FirstStep("direct:r:2:s:1", "direct:r:2:s:2", "processor6");
//        var ss2 = new Step("direct:r:2:s:2", "direct:r:2:s:3", "processor7");
//        var ss3 = new Step("direct:r:2:s:3", "direct:r:2:s:4", "processor8");
//        var ss4 = new LastStepBm("direct:r:2:s:s4", "processor9");

        context.buildRoad(s1, s2, s3, s4, s5);
    }
}
