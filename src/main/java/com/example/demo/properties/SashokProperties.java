package com.example.demo.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

@ConfigurationProperties(prefix = "sashok")
public class SashokProperties {

    @NestedConfigurationProperty
    private Kafka kafka = new Kafka();

    public Kafka getKafka() {
        return kafka;
    }

    public void setKafka(Kafka kafka) {
        this.kafka = kafka;
    }

    public static class Kafka {
        private String bootstrapServer;
        private String topic;

        public String getBootstrapServer() {
            return bootstrapServer;
        }

        public void setBootstrapServer(String bootstrapServer) {
            this.bootstrapServer = bootstrapServer;
        }

        public String getTopic() {
            return topic;
        }

        public void setTopic(String topic) {
            this.topic = topic;
        }
    }
}
