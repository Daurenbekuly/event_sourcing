package com.example.demo.route.processor;

import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import com.example.demo.common.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.FIRST_STEP_PROCESSOR;

@Service(FIRST_STEP_PROCESSOR)
public class FirstStepProcessor implements Processor {

    private final PostgresRepository postgresRepository;

    public FirstStepProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        if (baseModel.sashokId() == 0) {
            Long sashokId = postgresRepository.active(baseModel);
            BaseModel active = new BaseModel(baseModel, sashokId);
            String json = JsonUtil.toJson(active).orElseThrow();
            exchange.getIn().setBody(json);
        }
    }
}
