package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashOkProcessor;
import com.example.demo.common.JsonUtil;
import org.springframework.stereotype.Service;

@Service
public class Processor2 extends AbstractSashOkProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = JsonUtil.toObject(jsonValue, ListNode.class).orElseThrow();
        ListNode listNode1 = new ListNode("Processor2", listNode);
        log.info(jsonValue);
//        throw new RuntimeException("XDD bro");
//        try {
//            Thread.sleep(Duration.ofMinutes(1));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return JsonUtil.toJson(listNode1).orElseThrow();
    }
}
