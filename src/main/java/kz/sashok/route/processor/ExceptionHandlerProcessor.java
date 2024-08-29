package kz.sashok.route.processor;

import kz.sashok.repository.postgres.PostgresRepository;
import kz.sashok.route.model.BaseModel;
import kz.sashok.common.JsonUtil;
import kz.sashok.common.Constant;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

@Service(Constant.EXCEPTION_HANDLER_PROCESSOR)
public class ExceptionHandlerProcessor implements Processor {

    private final PostgresRepository postgresRepository;

    public ExceptionHandlerProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        Exception exception = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
        postgresRepository.error(baseModel, new Exception(exception));
    }
}
