package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;

@Service
public class Processor1 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode1 = new ListNode("Processor1");
        log.info(jsonValue);
        return toJsonOrElseThrow(listNode1);
    }
}
