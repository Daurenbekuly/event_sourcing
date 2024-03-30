package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.route.common.Constant;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.route.common.Constant.MAIN_ROUTE_RECEIVER;
import static com.example.demo.route.common.Constant.RECEIVER;

@Service(Constant.INIT_SUB_ROUTE_PROCESSOR)
public class InitSubProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String receiver = exchange.getIn().getHeader(RECEIVER, String.class);
        String correlation = exchange.getIn().getHeader(MAIN_ROUTE_RECEIVER, String.class);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        baseModel.mainRoadSteps().add(correlation);
        BaseModel newBaseModel = new BaseModel(baseModel, receiver);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
