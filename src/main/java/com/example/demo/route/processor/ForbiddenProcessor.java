package com.example.demo.route.processor;

import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.FORBIDDEN_PROCESSOR;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(FORBIDDEN_PROCESSOR)
public class ForbiddenProcessor implements Processor {

    private final PostgresRepository postgresRepository;

    public ForbiddenProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        postgresRepository.forbidden(baseModel);
    }
}
