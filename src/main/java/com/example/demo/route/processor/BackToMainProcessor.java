package com.example.demo.route.processor;

import com.example.demo.route.model.BaseModel;
import com.example.demo.common.Constant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.Stack;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(Constant.BACK_TO_MAIN_ROUTE_PROCESSOR)
public class BackToMainProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        Stack<String> mainRouteSteps = baseModel.mainRouteSteps();
        if (mainRouteSteps.isEmpty()) {
            exchange.setRouteStop(true);
        } else {
            String receiver = mainRouteSteps.pop();
            BaseModel newBaseModel = new BaseModel(baseModel, receiver, mainRouteSteps);
            String json = toJsonOrElseThrow(newBaseModel);
            exchange.getIn().setBody(json);
        }
    }
}
