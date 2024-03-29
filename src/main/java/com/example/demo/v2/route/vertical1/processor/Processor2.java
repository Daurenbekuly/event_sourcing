package com.example.demo.v2.route.vertical1.processor;

import com.example.demo.v2.route.sashok_processor.SashOkProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor2 extends SashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        jsonValue += " Processor2";
//        throw new RuntimeException("XDD");
        System.out.println(jsonValue);
        return jsonValue;
    }
}
