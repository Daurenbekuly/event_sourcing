package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service
public class Processor2 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = toObjectOrElseThrow(jsonValue, ListNode.class);
        ListNode listNode1 = new ListNode("Processor2", listNode);
        log.info(jsonValue);
//        throw new RuntimeException();
//        try {
//            Thread.sleep(Duration.ofMinutes(1));
//        } catch (InterruptedException e) {
//            throw new RuntimeException(e);
//        }
        return toJsonOrElseThrow(listNode1);
    }
}
