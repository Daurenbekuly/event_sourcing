package com.example.demo.route.step;

import com.example.demo.entity.StepEntity;
import com.example.demo.model.BaseModel;
import com.example.demo.repository.SashokRepository;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.route.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static com.example.demo.route.common.Constant.TIMEOUT;
import static org.apache.camel.Exchange.REDELIVERY_COUNTER;
import static org.apache.camel.Exchange.REDELIVERY_MAX_COUNTER;


public abstract class AbstractSashOkStepBuilder extends RouteBuilder {

    protected String exceptionHandler = "direct:defErrorHandler";
    protected Integer maximumRedeliveries = 10;
    protected Double exceptionBackOffMultiplier = 2.0;
    protected Long redeliveryDelay = 1000L;

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
                .process(EXCEPTION_HANDLER_PROCESSOR)
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
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Map<String, UUID> road = baseModel.road();
        String exchangeId = exchange.getExchangeId();
        UUID uuid = UUID.nameUUIDFromBytes(exchangeId.getBytes());
        BaseModel newBaseModel = new BaseModel(baseModel, uuid, receiver, availableTryCount);

        long sec = (redeliveryDelay) / 1000;
        double pow = Math.pow(exceptionBackOffMultiplier * sec, current) + Math.divideExact(timeout + 10000L, 1000);
        Instant nextRetryDate = Instant.now().plusSeconds((long) pow);
        StepEntity stepEntity = new StepEntity(newBaseModel, nextRetryDate); //todo update only retry count
        StepEntity saved = SashokRepository.step().save(stepEntity);
        road.put(baseModel.receiverName(), saved.getStepId());
        if (current == 1) SashokRepository.jdbc().retry(newBaseModel);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }

    public void nameValidator(String name) {
        String pattern = "direct:.*?:(\\d+):.*?:(\\d+)";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(name);
        if (!matcher.matches()) throw new RuntimeException("Name is not valid, validator patter: " + pattern);
    }
}
