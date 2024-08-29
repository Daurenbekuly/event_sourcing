package kz.sashok.route.processor;

import kz.sashok.route.model.BaseModel;
import kz.sashok.common.Constant;
import kz.sashok.common.JsonUtil;
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
        Stack<String> mainRouteSteps = baseModel.mainRouteSteps();
        if (mainRouteSteps.isEmpty()) {
            exchange.setRouteStop(true);
        } else {
            String receiver = mainRouteSteps.pop();
            BaseModel newBaseModel = new BaseModel(baseModel, receiver, mainRouteSteps);
            String json = JsonUtil.toJson(newBaseModel).orElseThrow();
            exchange.getIn().setBody(json);
        }
    }
}
