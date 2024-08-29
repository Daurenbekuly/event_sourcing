package kz.sashok.route.processor;

import kz.sashok.route.model.BaseModel;
import kz.sashok.common.Constant;
import kz.sashok.common.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service(Constant.INIT_SUB_ROUTE_PROCESSOR)
public class InitSubProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String subRouteReceiver = exchange.getIn().getHeader(Constant.RECEIVER, String.class);
        String mainRouteReceiver = exchange.getIn().getHeader(Constant.MAIN_ROUTE_RECEIVER, String.class);
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        baseModel.mainRouteSteps().push(mainRouteReceiver);
        BaseModel newBaseModel = new BaseModel(baseModel, subRouteReceiver);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }
}
