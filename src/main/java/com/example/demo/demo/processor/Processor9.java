package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service
public class Processor9 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = toObjectOrElseThrow(jsonValue, ListNode.class);
        ListNode listNode1 = new ListNode("Processor9", listNode);
        log.info(jsonValue);
        throw new RuntimeException();
//        return toJsonOrElseThrow(listNode1);
    }
}
