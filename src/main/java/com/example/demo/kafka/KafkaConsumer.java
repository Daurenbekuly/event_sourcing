package com.example.demo.kafka;

import com.example.demo.entity.StepEntity;
import com.example.demo.model.BaseModel;
import com.example.demo.repository.StepRepository;
import com.example.demo.util.JsonUtil;
import org.apache.camel.ProducerTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class KafkaConsumer {

    private final ProducerTemplate template;
    private final StepRepository stepRepository;

    public KafkaConsumer(ProducerTemplate template,
                         StepRepository stepRepository) {
        this.template = template;
        this.stepRepository = stepRepository;
    }

    @KafkaListener(topics = "${app.kafka.topic.sashok}", groupId = "${app.kafka.group.step}", concurrency = "5")
    public void stepListener(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.asyncRequestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.sashok}", groupId = "${app.kafka.group.cassandra}", concurrency = "5")
    public void cassandraListener(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        var stepEntity = new StepEntity(baseModel);
        stepRepository.save(stepEntity);
    }

}
