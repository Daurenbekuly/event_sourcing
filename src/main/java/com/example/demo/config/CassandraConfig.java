package com.example.demo.config;

import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;

@Configuration
public class CassandraConfig extends AbstractCassandraConfiguration {

    @NotNull
    @Override
    public String getKeyspaceName() {
        return "sashok";
    }

    @NotNull
    @Override
    public String getContactPoints() {
        return "localhost";
    }

    @Override
    public int getPort() {
        return 55001;
    }

}
