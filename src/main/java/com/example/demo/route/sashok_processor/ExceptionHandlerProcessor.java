package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.repository.SashokRepository;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.route.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static org.apache.camel.ExchangePropertyKey.EXCEPTION_CAUGHT;

@Service(EXCEPTION_HANDLER_PROCESSOR)
public class ExceptionHandlerProcessor implements Processor {

    private final SashokRepository sashokRepository;

    public ExceptionHandlerProcessor(SashokRepository sashokRepository) {
        this.sashokRepository = sashokRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Exception exception = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
        sashokRepository.jdbc().error(baseModel, new Exception(exception));
    }
}
