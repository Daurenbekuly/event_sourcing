package com.example.demo.consumer;

import com.example.demo.repository.postgres.PostgresRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.example.demo.common.JsonUtil.toObjectOrElseThrow;

@Component
public class KafkaReadConsumer extends RouteBuilder {

    private final ProducerTemplate template;
    private final PostgresRepository postgresRepository;

    @Value("${app.kafka.topic.sashok}")
    private String topic;

    @Value("${app.kafka.bootstrap-servers}")
    private String broker;

    @Value("${app.kafka.group.read}")
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
                "&autoOffsetReset=earliest" +
                "&maxPollRecords=100" +
                "&consumersCount=1";
        from(uri)
                .process(this::read)
                .end();
    }

    public void read(Exchange exchange) {
        var body = exchange.getIn().getBody().toString();
        var baseModel = toObjectOrElseThrow(body, BaseModel.class);
        if (postgresRepository.isCancelled(baseModel)) return;
        template.asyncRequestBody(baseModel.receiverName(), body);
    }
}
