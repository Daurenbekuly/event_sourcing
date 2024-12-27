package com.example.demo.config;

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
        return "host.docker.internal";
    }

    @Override
    public int getPort() {
        return 55001;
    }

}
