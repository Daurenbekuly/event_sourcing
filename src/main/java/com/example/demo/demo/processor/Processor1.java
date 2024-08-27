package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashokProcessor;
import com.example.demo.common.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class Processor1 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = JsonUtil.toObject(jsonValue, ListNode.class).orElseThrow();
        ListNode listNode1 = new ListNode("Processor1", listNode);
        log.info(jsonValue);
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
