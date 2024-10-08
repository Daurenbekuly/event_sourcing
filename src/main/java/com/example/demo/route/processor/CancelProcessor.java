package com.example.demo.route.processor;

import com.example.demo.common.JsonUtil;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.CANCEL_PROCESSOR;

@Service(CANCEL_PROCESSOR)
public class CancelProcessor implements Processor {

    private final PostgresRepository postgresRepository;

    public CancelProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        postgresRepository.cancel(baseModel);
    }
}
