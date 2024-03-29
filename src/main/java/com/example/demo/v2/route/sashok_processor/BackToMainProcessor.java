package com.example.demo.v2.route.sashok_processor;

import com.example.demo.model.Step;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Queue;

import static com.example.demo.v2.route.common.Constant.BACK_TO_MAIN_PROCESS;

@Service(BACK_TO_MAIN_PROCESS)
public class BackToMainProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        Step step = JsonUtil.toObject(body, Step.class).orElseThrow();
        Queue<String> correlations = step.correlations();
        if (correlations.isEmpty()) exchange.setRouteStop(true);
        String receiver = correlations.poll();
        Step newStep = new Step(step.receiverName(), receiver, step.jsonValue(), Instant.now(), 5, step.road(), correlations);
        String json = JsonUtil.toJson(newStep).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
