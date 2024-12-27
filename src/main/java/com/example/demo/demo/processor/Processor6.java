package com.example.demo.demo.processor;

import com.example.demo.demo.ListNode;
import com.example.demo.route.processor.AbstractSashokProcessor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service
public class Processor6 extends AbstractSashokProcessor {

    @Override
    public String invoke(String jsonValue) {
        ListNode listNode = toObjectOrElseThrow(jsonValue, ListNode.class);
        ListNode listNode1 = new ListNode("Processor6", listNode);
        log.info(jsonValue);
        return toJsonOrElseThrow(listNode1);
    }
}
