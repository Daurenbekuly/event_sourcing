package com.example.demo.route.processor;

import com.example.demo.route.sashok_processor.SashOkProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor9 extends SashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        jsonValue += " Processor9";
        System.out.println(jsonValue);
        return jsonValue;
    }
}
