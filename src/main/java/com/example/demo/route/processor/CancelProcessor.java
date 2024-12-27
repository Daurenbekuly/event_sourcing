package com.example.demo.route.processor;

import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.CANCEL_PROCESSOR;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(CANCEL_PROCESSOR)
public class CancelProcessor implements Processor {

    private final PostgresRepository postgresRepository;

    public CancelProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        postgresRepository.cancel(baseModel);
    }
}
