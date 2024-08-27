package com.example.demo.route.processor;

import com.example.demo.route.model.BaseModel;
import com.example.demo.common.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static com.example.demo.repository.SashokRepository.postgres;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

@Service(EXCEPTION_HANDLER_PROCESSOR)
public class ExceptionHandlerProcessor implements Processor {

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Exception exception = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
        postgres().error(baseModel, new Exception(exception));
    }
}
