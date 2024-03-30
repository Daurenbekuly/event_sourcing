package com.example.demo.config;

import org.apache.camel.CamelContext;
import org.apache.camel.Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@Configuration
public class CamelConfig {

    private final ApplicationContext applicationContext;

    public CamelConfig(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Bean
    public CamelContext camelContext() throws Exception {
        CamelContext context = applicationContext.getBean("camelContext", CamelContext.class);
        context.setAutoStartup(false);
        return context;
    }
}
