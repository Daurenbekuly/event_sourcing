package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import kz.sashok.route.processor.AbstractSashokProcessor;
import kz.sashok.common.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class Processor8 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = JsonUtil.toObject(jsonValue, ListNode.class).orElseThrow();
        ListNode listNode1 = new ListNode("Processor8", listNode);
        log.info(jsonValue);
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
