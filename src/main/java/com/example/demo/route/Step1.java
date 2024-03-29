//package com.example.demo.route;
//
//import com.example.demo.castom_bean_config.Step;
//import com.example.demo.v2.route.StepX;
//import org.springframework.beans.factory.annotation.Value;
//
//@Step(name = "direct:step1", receiverName = "direct:step2")
//public class Step1 extends StepX {
//
//    protected Step1(@Value("${app.s.1.from}") String from,
//                    @Value("${app.s.1.to}") String to,
//                    @Value("${app.s.1.eh-from}") String exceptionHandler) {
//        super(from, to, exceptionHandler);
//    }
//
//    @Override
//    public String invoke(String jsonValue) {
//        System.out.println("step1 " + jsonValue);
//        return "step1 " + jsonValue;
//    }
//}
