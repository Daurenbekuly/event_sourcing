package com.example.demo.repository;

import com.example.demo.model.Step;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SashokJdbcRepo {

    private final JdbcClient client;

    public SashokJdbcRepo(JdbcClient client) {
        this.client = client;
    }


    public void create(Step step) {
        LocalDateTime now = LocalDateTime.now();
        client.sql("insert into sashok(id, json_value, road, stat_date) values (DEFAULT, ?, ?, ?)")
                .params(List.of(step.jsonValue(), step.road().toString(), now))
                .update();
    }
}
