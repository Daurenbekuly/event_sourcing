package com.example.demo.route.processor;

import com.example.demo.repository.cassandra.StepRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.route.model.BaseModel;
import com.example.demo.repository.postgres.PostgresRepository;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.Constant.LAST_STEP_PROCESSOR;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(LAST_STEP_PROCESSOR)
public class LastStepProcessor implements Processor {

    private final PostgresRepository postgresRepository;
    private final StepRepository stepRepository;

    public LastStepProcessor(PostgresRepository postgresRepository,
                             StepRepository stepRepository) {
        this.postgresRepository = postgresRepository;
        this.stepRepository = stepRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        var stepEntity = new StepEntity(baseModel);
        stepRepository.save(stepEntity);
        postgresRepository.success(baseModel);
    }
}
