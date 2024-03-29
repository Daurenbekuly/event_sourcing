package com.example.demo.v2.route.sashok_route;

import com.example.demo.model.Step;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumerRoute extends RouteBuilder {

    private final ProducerTemplate template;

    public KafkaConsumerRoute(ProducerTemplate template) {
        this.template = template;
    }

    @Override
    public void configure() throws Exception {
        from("kafka:es-topic?brokers=localhost:29092")
                .process(this::processing)
                .end();
    }

    private void processing(Exchange exchange) {
        String body = exchange.getIn().getBody().toString();
        Step step = JsonUtil.toObject(body, Step.class).orElseThrow();
        String json = JsonUtil.toJson(step).orElseThrow();
        template.asyncRequestBody(step.receiverName(), json);
    }
}
