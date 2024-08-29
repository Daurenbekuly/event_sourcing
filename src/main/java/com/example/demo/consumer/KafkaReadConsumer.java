package com.example.demo.consumer;

import com.example.demo.common.JsonUtil;
import com.example.demo.repository.cassandra.StoppedRouteRepository;
import com.example.demo.route.model.BaseModel;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaReadConsumer extends RouteBuilder {

    private final ProducerTemplate template;
    private final StoppedRouteRepository stoppedRouteRepository;

    @Value("${app.kafka.topic.sashok}")
    private String topic;

    @Value("${app.kafka.bootstrap-servers}")
    private String broker;

    @Value("${app.kafka.group.step}")
    private String group;

    public KafkaReadConsumer(ProducerTemplate template,
                             StoppedRouteRepository stoppedRouteRepository) {
        this.template = template;
        this.stoppedRouteRepository = stoppedRouteRepository;
    }

    @Override
    public void configure() {
        String uri = "kafka:" + topic +
                "?brokers=" + broker +
                "&groupId=" + group +
                "&autoOffsetReset=latest" +
                "&consumersCount=1";
        from(uri)
                .process(this::read)
                .end();
    }

    public void read(Exchange exchange) {
        var body = exchange.getIn().getBody().toString();
        var baseModel = JsonUtil.toObject(body, BaseModel.class)
                .orElseThrow(() -> new RuntimeException("Error KafkaConsumer toObject"));
        if (stoppedRouteRepository.isCancelled(baseModel)) return;
        template.asyncRequestBody(baseModel.receiverName(), body);
    }
}
