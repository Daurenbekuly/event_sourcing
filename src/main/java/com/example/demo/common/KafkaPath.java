package com.example.demo.common;

import com.example.demo.properties.SashokProperties;
import org.springframework.stereotype.Component;

@Component
public class KafkaPath {

    public static String KAFKA_PATH_SASHOK;

    public KafkaPath(SashokProperties sashokProperties) {
        KafkaPath.KAFKA_PATH_SASHOK = toPath(sashokProperties);
    }

    public static String toPath(SashokProperties sashokProperties) {
        var topic = sashokProperties.getKafka().getTopic();
        var bootstrapServer = sashokProperties.getKafka().getBootstrapServer();
        return "kafka:" + topic + "?brokers=" + bootstrapServer;
    }
}
