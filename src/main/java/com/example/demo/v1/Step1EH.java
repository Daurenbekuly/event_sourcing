//package com.example.demo.route;
//
//import com.example.demo.v2.route.StepX;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Step1EH extends StepX {
//
//    protected Step1EH(@Value("${app.s.1.eh-from}") String from) {
//        super(from);
//    }
//
//    @Override
//    public String invoke(String jsonValue) {
//        System.out.println("step1errorHandler " + jsonValue);
//        return "step1errorHandler " + jsonValue;
//    }
//}
