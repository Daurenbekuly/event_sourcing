package kz.sashok.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaPath {

    public static String KAFKA_PATH_SASHOK;

    public KafkaPath(@Value("${app.kafka.bootstrap-servers}") String bootstrapServers,
                     @Value("${app.kafka.topic.sashok}") String sashok,
                     @Value("${app.kafka.producer-additional}") String additional) {
        KafkaPath.KAFKA_PATH_SASHOK = toPath(sashok, bootstrapServers, additional);
    }

    public static String toPath(String topic, String bootstrapServers, String additional) {
        return "kafka:" + topic + "?brokers=" + bootstrapServers + additional;
    }
}
