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

    /*
    @KafkaListener(topics = "${app.kafka.topic.thirty-min-retry}", groupId = "${app.kafka.group.thirty-min-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 60000L), //MIN_10
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void thirtyMinRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);

    @KafkaListener(topics = "${app.kafka.topic.one-hours-retry}", groupId = "${app.kafka.group.one-hours-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 1200000L), //MIN_20
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void oneHoursRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.two-hour-retry}", groupId = "${app.kafka.group.two-hour-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 2400000L), //MIN_40
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void twoHourRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.four-hours-retry}", groupId = "${app.kafka.group.four-hours-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 4800000L), //HRS_1_MIN_20
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void fourHoursRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.eight-hours-retry}", groupId = "${app.kafka.group.eight-hours-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 9600000L), //HRS_2_MIN_40
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void eightHoursRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.sixteen-hours-retry}", groupId = "${app.kafka.group.sixteen-hours-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 19200000L), //HRS_5_MIN_20
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void sixteenHoursRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.thirty-hours-retry}", groupId = "${app.kafka.group.thirty-hours-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 38400000L), //HRS_10_MIN_40
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void thirtyHoursRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }

    @KafkaListener(topics = "${app.kafka.topic.five-days-retry}", groupId = "${app.kafka.group.five-days-retry}")
    @RetryableTopic(
            backoff = @Backoff(value = 153600000L), //DAY_1_HRS_18_MIN_40
            attempts = "4",
            autoCreateTopics = "false",
            include = RuntimeException.class)
    public void fiveDaysRetry(String message) {
        var baseModel = JsonUtil.toObject(message, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        template.requestBody(baseModel.receiverName(), message);
    }
     */
}
