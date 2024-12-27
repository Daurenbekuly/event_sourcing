package com.example.demo.consumer;

import com.example.demo.repository.cassandra.StepRepository;
import com.example.demo.repository.cassandra.entity.StepEntity;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Component
public class KafkaWriteConsumer extends RouteBuilder {

    private final StepRepository stepRepository;

    @Value("${app.kafka.topic.sashok}")
    private String topic;

    @Value("${app.kafka.bootstrap-servers}")
    private String broker;

    @Value("${app.kafka.group.write}")
    private String group;

    public KafkaWriteConsumer(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }


    @Override
    public void configure() {
        String uri = "kafka:" + topic +
                "?brokers=" + broker +
                "&groupId=" + group +
                "&autoOffsetReset=latest" +
                "&maxPollRecords=10" +
                "&consumersCount=1";
        from(uri)
                .process(this::write)
                .end();
    }

    public void write(Exchange exchange) {
        var body = exchange.getIn().getBody().toString();
        var baseModel = toObjectOrElseThrow(body, BaseModel.class);
        var stepEntity = new StepEntity(baseModel);
        stepRepository.save(stepEntity);
    }
}
