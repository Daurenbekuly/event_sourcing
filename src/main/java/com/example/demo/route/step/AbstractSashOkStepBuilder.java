package com.example.demo.route.step;

import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.common.Constant.DEFAULT_EXCEPTION_BACK_OFF_MULTI;
import static com.example.demo.common.Constant.DEFAULT_EXECUTION_TIME_TO_WAIT;
import static com.example.demo.common.Constant.DEFAULT_MAX_REDELIVERIES;
import static com.example.demo.common.Constant.DEFAULT_REDELIVERY_DELAY;
import static com.example.demo.repository.SashokRepository.*;
import static com.example.demo.repository.SashokRepository.cassandra;
import static com.example.demo.common.Constant.DEFAULT_EXCEPTION_HANDLER_PROCESSOR;
import static com.example.demo.common.Constant.TIMEOUT;
import static com.example.demo.common.JsonUtil.*;
import static org.apache.camel.Exchange.REDELIVERY_COUNTER;
import static org.apache.camel.Exchange.REDELIVERY_MAX_COUNTER;

public abstract class AbstractSashOkStepBuilder extends RouteBuilder {

    protected String exceptionHandler = DEFAULT_EXCEPTION_HANDLER_PROCESSOR;
    protected Integer maximumRedeliveries = DEFAULT_MAX_REDELIVERIES;
    protected Double exceptionBackOffMultiplier = DEFAULT_EXCEPTION_BACK_OFF_MULTI;
    protected Long redeliveryDelay = DEFAULT_REDELIVERY_DELAY;
    protected Long executionTimeToWait = DEFAULT_EXECUTION_TIME_TO_WAIT;

    @Override
    public void configure() {
        onException(Exception.class)
                .log("Handling error: ${exception.stacktrace}")
                .maximumRedeliveryDelay(Long.MAX_VALUE)
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .backOffMultiplier(exceptionBackOffMultiplier)
                .useExponentialBackOff()
                .onRedelivery(this::reduceRetryCount)
                .logRetryAttempted(true)
                .log("Message Exhausted after " + maximumRedeliveries + " retries...")
                .handled(true)
                .process(exceptionHandler)
                .end();

        declareStep();
    }

    public abstract void declareStep();

    public void reduceRetryCount(Exchange exchange) {
        String receiver = exchange.getIn().getHeader("receiver", String.class);
        Integer current = exchange.getIn().getHeader(REDELIVERY_COUNTER, Integer.class);
        Integer max = exchange.getIn().getHeader(REDELIVERY_MAX_COUNTER, Integer.class);
        Long timeout = exchange.getIn().getHeader(TIMEOUT, Long.class);
        Integer availableTryCount = max - current;
        log.info("Current try {} of {}", current, max);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObject(body, BaseModel.class).orElseThrow();
        Map<String, UUID> road = baseModel.road();
        String exchangeId = exchange.getExchangeId();
        UUID uuid = UUID.nameUUIDFromBytes(exchangeId.getBytes());
        BaseModel newBaseModel = new BaseModel(baseModel, uuid, receiver, availableTryCount);

        long sec = (redeliveryDelay) / 1000;
        double pow = Math.pow(exceptionBackOffMultiplier * sec, current) + Math.divideExact(timeout + 10000L, 1000);
        Instant nextRetryDate = Instant.now().plusSeconds((long) pow);
        StepEntity stepEntity = new StepEntity(newBaseModel, nextRetryDate);
        StepEntity saved = cassandra().step().save(stepEntity);
        road.put(baseModel.receiverName(), saved.getStepId());
        if (current == 1) postgres().retry(newBaseModel);
        String json = toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }

    public static void nameValidator(String name) {
        String pattern = "direct:r.*?:(\\d+):s.*?:(\\d+)";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(name);
        if (!matcher.matches()) throw new RuntimeException("Name " + name + "is not valid, validator patter: " + pattern);
    }
}
