package kz.sashok.route.processor;

import kz.sashok.common.JsonUtil;
import kz.sashok.repository.postgres.PostgresRepository;
import kz.sashok.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static kz.sashok.common.Constant.CANCEL_PROCESSOR;

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
