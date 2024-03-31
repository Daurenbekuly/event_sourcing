package com.example.demo.route.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.sashok_processor.SashOkProcessor;
import com.example.demo.util.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class Processor2 extends SashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = JsonUtil.toObject(jsonValue, ListNode.class).orElseThrow();
        ListNode listNode1 = new ListNode("Processor2", listNode);
        System.out.println(jsonValue);
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
