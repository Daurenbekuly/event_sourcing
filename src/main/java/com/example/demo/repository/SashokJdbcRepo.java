package com.example.demo.repository;

import com.example.demo.model.BaseModel;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class SashokJdbcRepo {

    private final JdbcClient client;

    public SashokJdbcRepo(JdbcClient client) {
        this.client = client;
    }

    public Integer create(BaseModel baseModel) {
        LocalDateTime now = LocalDateTime.now();
        return client.sql("insert into sashok(id, json_value, start_date, status) values (DEFAULT, ?, ?, ?)")
                .params(List.of(baseModel.jsonValue(), now, "ACTIVE"))
                .update();
    }

    @Transactional
    public void error(BaseModel baseModel, Exception exception) {
        LocalDateTime now = LocalDateTime.now();
        client.sql("insert into sashok(id, json_value, road, end_date, status) values (?, ?, ?, ?, ?)")
                .params(List.of(baseModel.sashokId(), baseModel.jsonValue(), baseModel.road().toString(), now, "ERROR"))
                .update();

        client.sql("insert into error_message(id, sashok_id, message, stack_trace, create_date) values (DEFAULT, ?, ?, ?)")
                .params(List.of(baseModel.sashokId(), exception.getMessage(), exception.getStackTrace(), now))
                .update();
    }

    public void dene(BaseModel baseModel) {
        LocalDateTime now = LocalDateTime.now();
        client.sql("insert into sashok(id, json_value, road, end_date, status) values (?, ?, ?, ?, ?)")
                .params(List.of(baseModel.sashokId(), baseModel.jsonValue(), baseModel.road().toString(), now, "SUCCESS"))
                .update();
    }
}
