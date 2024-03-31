package com.example.demo.route.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class KafkaPath {

    public static String KAFKA_PATH_SASHOK;
    public static String KAFKA_PATH_THIRTY_MIN_RETRY;
    public static String KAFKA_PATH_ONE_HOURS_RETRY;
    public static String KAFKA_PATH_TWO_HOUR_RETRY;
    public static String KAFKA_PATH_FOUR_HOURS_RETRY;
    public static String KAFKA_PATH_EIGHT_HOURS_RETRY;
    public static String KAFKA_PATH_SIXTEEN_HOURS_RETRY;
    public static String KAFKA_PATH_THIRTY_HOURS_RETRY;
    public static String KAFKA_PATH_FIVE_DAYS_RETRY;

    public KafkaPath(@Value("${app.kafka.bootstrap-servers}") String bootstrapServers,
                     @Value("${app.kafka.topic.sashok}") String sashok,
                     @Value("${app.kafka.topic.thirty-min-retry}") String thirtyMinRetry,
                     @Value("${app.kafka.topic.one-hours-retry}") String oneHoursRetry,
                     @Value("${app.kafka.topic.two-hour-retry}") String twoHourRetry,
                     @Value("${app.kafka.topic.four-hours-retry}") String fourHoursRetry,
                     @Value("${app.kafka.topic.eight-hours-retry}") String eightHoursRetry,
                     @Value("${app.kafka.topic.sixteen-hours-retry}") String sixteenHoursRetry,
                     @Value("${app.kafka.topic.thirty-hours-retry}") String thirtyHoursRetry,
                     @Value("${app.kafka.topic.five-days-retry}") String fiveDaysRetry) {
        KafkaPath.KAFKA_PATH_SASHOK = toPath(sashok, bootstrapServers);
        KafkaPath.KAFKA_PATH_THIRTY_MIN_RETRY = toPath(thirtyMinRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_ONE_HOURS_RETRY = toPath(oneHoursRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_TWO_HOUR_RETRY = toPath(twoHourRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_FOUR_HOURS_RETRY = toPath(fourHoursRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_EIGHT_HOURS_RETRY = toPath(eightHoursRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_SIXTEEN_HOURS_RETRY = toPath(sixteenHoursRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_THIRTY_HOURS_RETRY = toPath(thirtyHoursRetry, bootstrapServers);
        KafkaPath.KAFKA_PATH_FIVE_DAYS_RETRY = toPath(fiveDaysRetry, bootstrapServers);
    }

    public static String toPath(String topic, String bootstrapServers) {
        return "kafka:" + topic + "?brokers=" + bootstrapServers;
    }
}
