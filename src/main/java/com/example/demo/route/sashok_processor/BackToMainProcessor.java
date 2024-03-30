package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.route.common.Constant;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.Stack;

@Service(Constant.BACK_TO_MAIN_ROUTE_PROCESSOR)
public class BackToMainProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Stack<String> mainRoadSteps = baseModel.mainRoadSteps();
        if (mainRoadSteps.isEmpty()) exchange.setRouteStop(true);
        String receiver = mainRoadSteps.pop();
        BaseModel newBaseModel = new BaseModel(baseModel, receiver, mainRoadSteps);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
