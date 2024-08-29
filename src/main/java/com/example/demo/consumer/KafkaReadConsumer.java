package com.example.demo.consumer;

import com.example.demo.common.JsonUtil;
import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaReadConsumer extends RouteBuilder {

    private final ProducerTemplate template;
    private final PostgresRepository postgresRepository;

    @Value("${app.kafka.topic.sashok}")
    private String topic;

    @Value("${app.kafka.bootstrap-servers}")
    private String broker;

    @Value("${app.kafka.group.step}")
    private String group;

    public KafkaReadConsumer(ProducerTemplate template,
                             PostgresRepository postgresRepository) {
        this.template = template;
        this.postgresRepository = postgresRepository;
    }

    @Override
    public void configure() {
        String uri = "kafka:" + topic +
                "?brokers=" + broker +
                "&groupId=" + group +
                "&autoOffsetReset=latest" +
                "&maxPollRecords=100" +
                "&consumersCount=2";
        from(uri)
                .process(this::read)
                .end();
    }

    public void read(Exchange exchange) {
        var body = exchange.getIn().getBody().toString();
        var baseModel = JsonUtil.toObject(body, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        if (postgresRepository.isCancelled(baseModel)) return;
        template.asyncRequestBody(baseModel.receiverName(), body);
    }
}
