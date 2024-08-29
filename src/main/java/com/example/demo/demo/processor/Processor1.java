package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import kz.sashok.common.JsonUtil;
import kz.sashok.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor1 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode1 = new ListNode("Processor1");
        log.info(jsonValue);
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
