package com.example.demo.route.processor;

import com.example.demo.route.model.BaseModel;
import com.example.demo.common.Constant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.MAIN_ROUTE_RECEIVER;
import static com.example.demo.common.Constant.RECEIVER;
import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(Constant.INIT_SUB_ROUTE_PROCESSOR)
public class InitSubProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String subRouteReceiver = exchange.getIn().getHeader(RECEIVER, String.class);
        String mainRouteReceiver = exchange.getIn().getHeader(MAIN_ROUTE_RECEIVER, String.class);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        baseModel.mainRouteSteps().push(mainRouteReceiver);
        BaseModel newBaseModel = new BaseModel(baseModel, subRouteReceiver);
        String json = toJsonOrElseThrow(newBaseModel);
        exchange.getIn().setBody(json);
    }
}
