package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.example.demo.route.common.Constant.RECEIVER;

public abstract class SashOkProcessor implements Processor {

    @Override
    public void process(Exchange exchange) {
        String receiver = exchange.getIn().getHeader(RECEIVER, String.class);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Map<String, UUID> road = baseModel.road();
        UUID uuid = UUID.randomUUID();
        road.put(baseModel.receiverName(), uuid);
        String jsonValue = invoke(baseModel.jsonValue());
        BaseModel newBaseModel = new BaseModel(baseModel, uuid, receiver, jsonValue, road);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }

    public abstract String invoke(String jsonValue);

}
