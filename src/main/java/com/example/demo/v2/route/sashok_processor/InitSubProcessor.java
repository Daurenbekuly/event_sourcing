package com.example.demo.v2.route.sashok_processor;

import com.example.demo.model.Step;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.time.Instant;

import static com.example.demo.v2.route.common.Constant.INIT_SUB_PROCESS;

@Service(INIT_SUB_PROCESS)
public class InitSubProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String receiver = exchange.getIn().getHeader("receiver", String.class);
        String correlation = exchange.getIn().getHeader("correlation", String.class);
        String body = exchange.getIn().getBody().toString();
        Step step = JsonUtil.toObject(body, Step.class).orElseThrow();
        step.correlations().add(correlation);
        Step newStep = new Step(step.receiverName(), receiver, step.jsonValue(), Instant.now(), 5, step.road(), step.correlations());
        String json = JsonUtil.toJson(newStep).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
