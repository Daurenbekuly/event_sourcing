package com.example.demo.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class CamelConfig {

    private final CamelContext context;

    public CamelConfig(CamelContext context) {
        this.context = context;
    }

    @Bean
    public CamelContext camelContext() {
        context.setAutoStartup(false);
        return context;
    }
}
