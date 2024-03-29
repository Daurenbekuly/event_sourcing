//package com.example.demo.route;
//
//import com.example.demo.v2.route.StepX;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Step2 extends StepX {
//
//    protected Step2(@Value("${app.s.2.from}") String from,
//                    @Value("${app.s.2.eh-from}") String exceptionHandler) {
//        super(from, "", exceptionHandler);
//    }
//
//    @Override
//    public String invoke(String jsonValue) {
//        System.out.println("step2 " + jsonValue);
////        throw new RuntimeException("step2 XDD");
//        return "step2 " + jsonValue;
//    }
//}
