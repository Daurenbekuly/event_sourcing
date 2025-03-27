package com.example.demo.route.processor;

import com.example.demo.common.CancelException;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static com.example.demo.common.Constant.EXCEPTION_HANDLER_PROCESSOR;
import static com.example.demo.common.Header.BACK_OFF_MULTIPLIER;
import static com.example.demo.common.Header.MAX_REDELIVERY;
import static com.example.demo.common.Header.REDELIVERY_DELAY;
import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;
import static org.apache.camel.Exchange.EXCEPTION_CAUGHT;

@Service(EXCEPTION_HANDLER_PROCESSOR)
public class ExceptionHandlerProcessor implements Processor {

    protected final Logger log = LogManager.getLogger(getClass());

    private final PostgresRepository postgresRepository;

    public ExceptionHandlerProcessor(PostgresRepository postgresRepository) {
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        Integer maxRetry = exchange.getIn().getHeader(MAX_REDELIVERY, Integer.class);
        Integer retryDelay = exchange.getIn().getHeader(REDELIVERY_DELAY, Integer.class);
        Double backOffMultiplier = exchange.getIn().getHeader(BACK_OFF_MULTIPLIER, Double.class);

        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        Integer currentRetry = postgresRepository.retryCount(baseModel);

        long retryDelaySec = retryDelay / 1000;
        double retrySec = Math.pow(2, currentRetry) * backOffMultiplier + retryDelaySec;
        LocalDateTime retryDate = LocalDateTime.now().plusSeconds((long) retrySec);

        if (isCancelled(baseModel)) throw new CancelException("Cancelled!");
        if (currentRetry == 1) {
            postgresRepository.onRetry(baseModel, retryDate, maxRetry);
            log.info("Current sashok: {} step: {} on retry",
                    baseModel.sashokId(), baseModel.receiverName());
        } else if (currentRetry <= maxRetry) {
            postgresRepository.updateRetry(baseModel, retryDate, currentRetry, maxRetry);
            log.info("Current sashok: {} step: {} try {} of {}",
                    baseModel.sashokId(), baseModel.receiverName(), currentRetry, maxRetry);
        } else {
            Exception exception = exchange.getProperty(EXCEPTION_CAUGHT, Exception.class);
            postgresRepository.error(baseModel, new Exception(exception));
            log.error("Sashok: {} step: {} exhausted after {} retries...",
                    baseModel.sashokId(), baseModel.receiverName(), maxRetry);
        }
    }

    private boolean isCancelled(BaseModel baseModel) {
        return postgresRepository.isCancelled(baseModel);
    }

}
