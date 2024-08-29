package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import kz.sashok.common.JsonUtil;
import kz.sashok.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

@Service
public class Processor4 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = JsonUtil.toObject(jsonValue, ListNode.class).orElseThrow();
        ListNode listNode1 = new ListNode("Processor4", listNode);
        log.info(jsonValue);
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
