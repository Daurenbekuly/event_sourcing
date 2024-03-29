package com.example.demo.v2.route.vertical1.processor;

import com.example.demo.v2.route.sashok_processor.SashOkProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor1 extends SashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        jsonValue += " Processor1";
//        throw new RuntimeException("XDD s1");
        System.out.println(jsonValue);
        return jsonValue;
    }
}
