package kz.sashok.route.processor;

import kz.sashok.repository.cassandra.CassandraRepository;
import kz.sashok.repository.cassandra.entity.StepEntity;
import kz.sashok.route.model.BaseModel;
import kz.sashok.repository.postgres.PostgresRepository;
import kz.sashok.common.JsonUtil;
import kz.sashok.common.Constant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service(Constant.LAST_STEP_PROCESSOR)
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
