package com.example.demo.v2.route.sashok_processor;

import com.example.demo.model.Step;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.time.Instant;

public abstract class SashOkProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String receiver = exchange.getIn().getHeader("receiver", String.class);
        String body = exchange.getIn().getBody().toString();
        Step step = JsonUtil.toObject(body, Step.class).orElseThrow();
        String jsonValue = invoke(step.jsonValue());
        Step newStep = new Step(step.receiverName(), receiver, jsonValue, Instant.now(), 5, step.road(), step.correlations());
        String json = JsonUtil.toJson(newStep).orElseThrow();
        exchange.getIn().setBody(json);
    }

    public abstract String invoke(String jsonValue);

}
