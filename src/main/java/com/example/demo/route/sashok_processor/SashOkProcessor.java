package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static com.example.demo.route.common.Constant.RECEIVER;
import static java.util.Objects.isNull;

public abstract class SashOkProcessor implements Processor {

    protected final Logger log = LogManager.getLogger(getClass());

    @Override
    public void process(Exchange exchange) throws ExecutionException, InterruptedException, TimeoutException {
        String receiver = exchange.getIn().getHeader(RECEIVER, String.class);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        Future<String> futureResult = executor.submit(() -> invoke(baseModel.jsonValue()));
        String jsonValue = futureResult.get(5000L, TimeUnit.MILLISECONDS);
        String receiverName = baseModel.receiverName();
        if (isLastStep(receiverName)) return;
        Map<String, UUID> road = baseModel.road();
        UUID uuid = UUID.randomUUID();
        road.put(receiverName, uuid);
        BaseModel newBaseModel = new BaseModel(baseModel, uuid, receiver, jsonValue, road);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }

    private static boolean isLastStep(String receiverName) {
        return isNull(receiverName);
    }

    public abstract String invoke(String jsonValue);

}
