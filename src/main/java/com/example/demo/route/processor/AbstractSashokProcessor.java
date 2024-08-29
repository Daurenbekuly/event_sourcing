package com.example.demo.route.processor;

import com.example.demo.common.CancelException;
import com.example.demo.repository.SashokRepository;
import com.example.demo.route.model.BaseModel;
import com.example.demo.common.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

import static com.example.demo.common.Constant.RECEIVER;
import static com.example.demo.common.Constant.TIMEOUT;
import static java.util.Objects.isNull;
import static java.util.concurrent.TimeUnit.*;

public abstract class AbstractSashokProcessor implements Processor {

    protected final Logger log = LogManager.getLogger(getClass());

    /**
     * Execute method invoke in new virtual thread
     * then set new basemodel with execution result
     *
     * @param exchange the message exchange
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        if (isCancelled(baseModel)) throw new CancelException("Cancelled!");

        String receiverName = baseModel.receiverName();
        if (isLastStep(receiverName)) return;

        UUID stepId = UUID.randomUUID();
        String receiver = exchange.getIn().getHeader(RECEIVER, String.class);
        String jsonValue = invoke(exchange, baseModel);
        Map<String, UUID> passedRoute = baseModel.passedRoute();
        passedRoute.put(receiverName, stepId);

        BaseModel newBaseModel = new BaseModel(baseModel, stepId, receiver, jsonValue, passedRoute);
        String json = JsonUtil.toJson(newBaseModel).orElseThrow();
        exchange.getIn().setBody(json);
    }

    private String invoke(Exchange exchange, BaseModel baseModel) throws InterruptedException, ExecutionException, TimeoutException {
        ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
        Future<String> future = executor.submit(() -> invoke(baseModel.jsonValue()));
        Long timeout = exchange.getIn().getHeader(TIMEOUT, Long.class);
        return future.get(timeout, MILLISECONDS);
    }

    protected abstract String invoke(String jsonValue);

    private boolean isLastStep(String receiverName) {
        return isNull(receiverName);
    }

    private boolean isCancelled(BaseModel baseModel) {
        return SashokRepository.cassandra().stoppedRoute().isCancelled(baseModel);
    }

}
