package com.example.demo.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaPath {

    public static String KAFKA_PATH_SASHOK;

    public KafkaPath(@Value("${app.kafka.bootstrap-servers}") String bootstrapServers,
                     @Value("${app.kafka.topic.sashok}") String sashok) {
        KafkaPath.KAFKA_PATH_SASHOK = toPath(sashok, bootstrapServers);
    }

    public static String toPath(String topic, String bootstrapServers) {
        return "kafka:" + topic + "?brokers=" + bootstrapServers;
    }
}
