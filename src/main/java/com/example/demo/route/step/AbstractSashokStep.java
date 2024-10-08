package com.example.demo.route.step;

import com.example.demo.common.CancelException;
import com.example.demo.repository.cassandra.entity.RetryEntity;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.demo.common.Constant.CANCEL_PROCESSOR;
import static com.example.demo.common.Constant.EXCEPTION_BACKOFF_MULTIPLIER;
import static com.example.demo.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static com.example.demo.common.Constant.EXECUTION_TIME_TO_WAIT;
import static com.example.demo.common.Constant.MAXIMUM_REDELIVERIES;
import static com.example.demo.common.Constant.RECEIVER;
import static com.example.demo.common.Constant.REDELIVERY_DELAY;
import static com.example.demo.common.Constant.TIMEOUT;
import static com.example.demo.common.JsonUtil.toJson;
import static com.example.demo.common.JsonUtil.toObject;
import static com.example.demo.repository.SashokRepository.cassandra;
import static com.example.demo.repository.SashokRepository.postgres;
import static org.apache.camel.Exchange.REDELIVERY_COUNTER;
import static org.apache.camel.Exchange.REDELIVERY_MAX_COUNTER;
import static org.apache.camel.LoggingLevel.ERROR;
import static org.apache.camel.LoggingLevel.WARN;

public abstract class AbstractSashokStep extends RouteBuilder {

    protected String exceptionHandler = EXCEPTION_HANDLER_PROCESSOR;
    protected Integer maximumRedeliveries = MAXIMUM_REDELIVERIES;
    protected Double exceptionBackOffMultiplier = EXCEPTION_BACKOFF_MULTIPLIER;
    protected Long redeliveryDelay = REDELIVERY_DELAY;
    protected Long executionTimeToWait = EXECUTION_TIME_TO_WAIT;

    @Override
    public void configure() {
        onException(CancelException.class)
                .log(WARN, "Handling error: ${exception.stacktrace}")
                .handled(true)
                .process(CANCEL_PROCESSOR)
                .end();

        onException(Exception.class)
                .log(ERROR, "Handling error: ${exception.stacktrace}")
                .maximumRedeliveryDelay(Long.MAX_VALUE)
                .maximumRedeliveries(maximumRedeliveries)
                .redeliveryDelay(redeliveryDelay)
                .backOffMultiplier(exceptionBackOffMultiplier)
                .useExponentialBackOff()
                .onRedelivery(this::reduceRetryCount)
                .log(ERROR, "Message Exhausted after " + maximumRedeliveries + " retries...")
                .handled(true)
                .process(exceptionHandler)
                .end();

        declareStep();
    }

    public abstract void declareStep();

    public void reduceRetryCount(Exchange exchange) {
        String receiver = exchange.getIn().getHeader(RECEIVER, String.class);
        Integer current = exchange.getIn().getHeader(REDELIVERY_COUNTER, Integer.class);
        Integer max = exchange.getIn().getHeader(REDELIVERY_MAX_COUNTER, Integer.class);
        Long timeout = exchange.getIn().getHeader(TIMEOUT, Long.class);
        Integer availableTryCount = max - current;
        log.info("Current try {} of {}", current, max);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObject(body, BaseModel.class).orElseThrow();
        Map<String, UUID> passedRoute = baseModel.passedRoute();
        String exchangeId = exchange.getExchangeId();
        UUID uuid = UUID.nameUUIDFromBytes(exchangeId.getBytes());
        BaseModel newBaseModel = new BaseModel(baseModel, uuid, receiver, availableTryCount);

        long redeliveryDelaySec = redeliveryDelay / 1000;
        long timeoutSec = timeout / 1000;
        double pow = (redeliveryDelaySec * exceptionBackOffMultiplier * (int) Math.pow(2, current - 1)) + timeoutSec + 1;
        Instant nextRetryDate = Instant.now().plusSeconds((long) pow);
        RetryEntity retryEntity = new RetryEntity(newBaseModel, nextRetryDate); //todo calc this
        cassandra().retry().save(retryEntity);

        if (current == 1) {
            StepEntity stepEntity = new StepEntity(newBaseModel);
            StepEntity saved = cassandra().step().save(stepEntity);
            passedRoute.put(baseModel.receiverName(), saved.getStepId());
            postgres().retry(newBaseModel);
        }
        String json = toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }

    public static void nameValidator(String name) {
        String pattern = "direct:r.*?:(\\d+):s.*?:(\\d+)";
        Pattern regexPattern = Pattern.compile(pattern);
        Matcher matcher = regexPattern.matcher(name);
        if (!matcher.matches())
            throw new RuntimeException("Name " + name + "is not valid, validator patter: " + pattern);
    }
}
