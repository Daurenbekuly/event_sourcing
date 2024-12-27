package com.example.demo.route.processor;

import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.FIRST_STEP_PROCESSOR;
import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(FIRST_STEP_PROCESSOR)
public class FirstStepProcessor implements Processor {

    private final PostgresRepository postgresRepository;

    public FirstStepProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        if (baseModel.sashokId() == 0) {
            Long sashokId = postgresRepository.active(baseModel);
            BaseModel active = new BaseModel(baseModel, sashokId);
            String json = toJsonOrElseThrow(active);
            exchange.getIn().setBody(json);
        }
    }
}
