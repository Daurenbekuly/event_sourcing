package com.example.demo.v2.route.sashok_route;

import com.example.demo.entity.StepEntity;
import com.example.demo.model.Step;
import com.example.demo.repository.StepRepository;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.OnExceptionDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import static org.apache.camel.Exchange.REDELIVERY_COUNTER;
import static org.apache.camel.Exchange.REDELIVERY_MAX_COUNTER;

public abstract class SashOkRouteBuilder extends RouteBuilder {

    public String exceptionHandler = "direct:defErrorHandler";
    public Integer maximumRedeliveries = 5;
    public Double exceptionBackOffMultiplier = 2.0;
    public Integer redeliveryDelay = 5000;

    @Autowired
    private StepRepository stepRepository;

    @Override
    public void configure() throws Exception {
        errorHandler(
                defaultErrorHandler()
                        .maximumRedeliveries(maximumRedeliveries)
                        .redeliveryDelay(redeliveryDelay)
        );


        onException(RuntimeException.class)
                .log("Handling error: ${exception.stacktrace}")
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .backOffMultiplier(exceptionBackOffMultiplier)
                .useExponentialBackOff()
                .onRedelivery(this::reduceRetryCount)
                .logRetryAttempted(true)
                .handled(true)
                .log("Message Exhausted after " + maximumRedeliveries + " retries...")
                .to(exceptionHandler)
                .end();

        buildRoute();
    }

    public abstract void buildRoute();

    public void reduceRetryCount(Exchange exchange) {
        String receiver = exchange.getIn().getHeader("receiver", String.class);
        Integer current = exchange.getIn().getHeader(REDELIVERY_COUNTER, Integer.class);
        Integer max = exchange.getIn().getHeader(REDELIVERY_MAX_COUNTER, Integer.class);
        Integer availableTryCount = max - current;
        log.info("Current try {} of {}", current, max);
        String body = exchange.getIn().getBody().toString();
        Step step = JsonUtil.toObject(body, Step.class).orElseThrow();
        Map<String, String> road = step.road();
        String exchangeId = exchange.getExchangeId();
        UUID uuid = UUID.nameUUIDFromBytes(exchangeId.getBytes());
        Step newStep = new Step(step.receiverName(), receiver, step.jsonValue(), Instant.now(), availableTryCount, road, step.correlations());
        StepEntity stepEntity = new StepEntity(uuid, newStep, availableTryCount);
        StepEntity saved = stepRepository.save(stepEntity);
        road.put(saved.getStepId().toString(), step.receiverName());
        String json = JsonUtil.toJson(newStep).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
