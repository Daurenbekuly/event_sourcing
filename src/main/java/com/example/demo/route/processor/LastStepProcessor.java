package com.example.demo.route.processor;

import com.example.demo.repository.cassandra.CassandraRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.route.model.BaseModel;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.common.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.LAST_STEP_PROCESSOR;

@Service(LAST_STEP_PROCESSOR)
public class LastStepProcessor implements Processor {

    private final PostgresRepository postgresRepository;
    private final CassandraRepository cassandraRepository;

    public LastStepProcessor(PostgresRepository postgresRepository,
                             CassandraRepository cassandraRepository) {
        this.postgresRepository = postgresRepository;
        this.cassandraRepository = cassandraRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        var stepEntity = new StepEntity(baseModel);
        cassandraRepository.step().save(stepEntity);
        postgresRepository.success(baseModel);
    }
}
