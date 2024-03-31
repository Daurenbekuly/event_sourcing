package com.example.demo.route.step;

import com.example.demo.entity.StepEntity;
import com.example.demo.model.BaseModel;
import com.example.demo.repository.StepRepository;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.UUID;

import static com.example.demo.route.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static org.apache.camel.Exchange.REDELIVERY_COUNTER;
import static org.apache.camel.Exchange.REDELIVERY_MAX_COUNTER;

@Component
public abstract class SashOkStepBuilder extends RouteBuilder {

    public String exceptionHandler = "direct:defErrorHandler";
    public Integer maximumRedeliveries = 2;
    public Double exceptionBackOffMultiplier = 2.0;
    public Integer redeliveryDelay = 1000;

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
                .log("Message Exhausted after " + maximumRedeliveries + " retries...") //todo if it has kafka retry send it to there
                .process(EXCEPTION_HANDLER_PROCESSOR)
                .handled(true)
                .end();

        declareStep();
    }

    public abstract void declareStep();

    public void reduceRetryCount(Exchange exchange) {
        String receiver = exchange.getIn().getHeader("receiver", String.class);
        Integer current = exchange.getIn().getHeader(REDELIVERY_COUNTER, Integer.class);
        Integer max = exchange.getIn().getHeader(REDELIVERY_MAX_COUNTER, Integer.class);
        Integer availableTryCount = max - current;
        log.info("Current try {} of {}", current, max);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Map<String, UUID> road = baseModel.road();
        String exchangeId = exchange.getExchangeId();
        UUID uuid = UUID.nameUUIDFromBytes(exchangeId.getBytes());
        BaseModel newBaseModel = new BaseModel(baseModel, uuid, receiver, availableTryCount);
        StepEntity stepEntity = new StepEntity(newBaseModel);
        StepEntity saved = stepRepository.save(stepEntity);
        road.put(baseModel.receiverName(), saved.getStepId());
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
