package com.example.demo.demo.infrastructure.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @Override
    public String getKeyspaceName() {
        return "sashok";
    }

    @Override
    public String getContactPoints() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 55001;
    }

}
