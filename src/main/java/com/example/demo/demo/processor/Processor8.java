package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

import java.time.Duration;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service
public class Processor8 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = toObjectOrElseThrow(jsonValue, ListNode.class);
        ListNode listNode1 = new ListNode("Processor8", listNode);
        log.info(jsonValue);
        try {
            Thread.sleep(Duration.ofSeconds(10));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return toJsonOrElseThrow(listNode1);
    }
}
