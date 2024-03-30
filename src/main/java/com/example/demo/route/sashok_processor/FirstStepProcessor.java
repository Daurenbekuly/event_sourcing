package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.repository.SashokJdbcRepo;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.example.demo.route.common.Constant.FIRST_STEP_PROCESSOR;
import static java.util.Objects.isNull;

@Service(FIRST_STEP_PROCESSOR)
public class FirstStepProcessor implements Processor {

    private final SashokJdbcRepo sashokJdbcRepo;

    public FirstStepProcessor(SashokJdbcRepo sashokJdbcRepo) {
        this.sashokJdbcRepo = sashokJdbcRepo;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        if (isNull(baseModel.sashokId())) {
            Integer sashokId = sashokJdbcRepo.create(baseModel);
            BaseModel active = new BaseModel(baseModel, sashokId);
            Optional<String> json = JsonUtil.toJson(active);
            exchange.getIn().setBody(json);
        }
    }
}
