package kz.sashok.route.processor;

import kz.sashok.common.Constant;
import kz.sashok.common.JsonUtil;
import kz.sashok.repository.cassandra.StepRepository;
import kz.sashok.repository.cassandra.entity.StepEntity;
import kz.sashok.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Service;

@Service(Constant.BEFORE_USER_TASK_PROCESSOR)
public class BeforeUserTaskProcessor implements Processor {

    private final StepRepository stepRepository;

    public BeforeUserTaskProcessor(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        String body = exchange.getIn().getBody().toString();
        BaseModel baseModel = JsonUtil.toObject(body, BaseModel.class).orElseThrow();
        var stepEntity = new StepEntity(baseModel);
        stepRepository.save(stepEntity);
    }
}
