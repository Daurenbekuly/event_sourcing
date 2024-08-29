package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import kz.sashok.route.processor.AbstractSashokProcessor;
import kz.sashok.common.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class Processor7 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = JsonUtil.toObject(jsonValue, ListNode.class).orElseThrow();
        ListNode listNode1 = new ListNode("Processor7", listNode);
        log.info(jsonValue);
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
