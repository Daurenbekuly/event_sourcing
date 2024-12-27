package com.example.demo.route.processor;

import com.example.demo.common.Constant;
import com.example.demo.repository.cassandra.StepRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Service(Constant.BEFORE_USER_TASK_PROCESSOR)
public class BeforeUserTaskProcessor implements Processor {

    private final StepRepository stepRepository;

    public BeforeUserTaskProcessor(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = toObjectOrElseThrow(body, BaseModel.class);
        var stepEntity = new StepEntity(baseModel);
        stepRepository.save(stepEntity);
    }
}
