//package com.example.demo.route;
//
//import com.example.demo.v2.route.StepX;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Step2EH extends StepX {
//
//    protected Step2EH(@Value("${app.s.2.eh-from}") String from,
//                      @Value("${app.s.2.eh-to}") String to) {
//        super(from, to);
//    }
//
//    @Override
//    public String invoke(String jsonValue) {
//        System.out.println("step2errorHandler " + jsonValue);
//        return "step2errorHandler " + jsonValue;
//    }
//}
