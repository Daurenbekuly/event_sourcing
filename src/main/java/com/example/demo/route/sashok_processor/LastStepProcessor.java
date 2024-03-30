package com.example.demo.route.sashok_processor;

import com.example.demo.model.BaseModel;
import com.example.demo.repository.SashokJdbcRepo;
import com.example.demo.util.JsonUtil;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;


import static com.example.demo.route.common.Constant.LAST_STEP_PROCESSOR;

@Service(LAST_STEP_PROCESSOR)
public class LastStepProcessor implements Processor {

    private final SashokJdbcRepo sashokJdbcRepo;

    public LastStepProcessor(SashokJdbcRepo sashokJdbcRepo) {
        this.sashokJdbcRepo = sashokJdbcRepo;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        sashokJdbcRepo.dene(baseModel);
    }
}
