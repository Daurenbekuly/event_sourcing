package com.example.demo.route.processor;

import com.example.demo.route.model.BaseModel;
import com.example.demo.common.Constant;
import com.example.demo.common.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.MAIN_ROUTE_RECEIVER;
import static com.example.demo.common.Constant.RECEIVER;

@Service(Constant.INIT_SUB_ROUTE_PROCESSOR)
public class InitSubProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String subRouteReceiver = exchange.getIn().getHeader(RECEIVER, String.class);
        String mainRouteReceiver = exchange.getIn().getHeader(MAIN_ROUTE_RECEIVER, String.class);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        baseModel.mainRoadSteps().push(mainRouteReceiver);
        BaseModel newBaseModel = new BaseModel(baseModel, subRouteReceiver);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
