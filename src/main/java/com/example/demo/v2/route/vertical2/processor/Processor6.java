package com.example.demo.v2.route.vertical2.processor;

import com.example.demo.v2.route.sashok_processor.SashOkProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor6 extends SashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        jsonValue += " Processor6";
        System.out.println(jsonValue);
        return jsonValue;
    }
}
