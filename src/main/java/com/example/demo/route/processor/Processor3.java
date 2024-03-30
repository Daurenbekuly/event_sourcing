package com.example.demo.route.processor;

import com.example.demo.route.sashok_processor.SashOkProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor3 extends SashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        jsonValue += " Processor3";
//        throw new RuntimeException("XDD");
        System.out.println(jsonValue);
        return jsonValue;
    }
}
