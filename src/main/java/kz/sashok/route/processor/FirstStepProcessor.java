package kz.sashok.route.processor;

import kz.sashok.repository.postgres.PostgresRepository;
import kz.sashok.route.model.BaseModel;
import kz.sashok.common.JsonUtil;
import kz.sashok.common.Constant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service(Constant.FIRST_STEP_PROCESSOR)
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
