package com.example.demo.repository;

import com.example.demo.model.BaseModel;
import com.example.demo.util.JsonUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Repository
public class SashokJdbcRepo {

    private final NamedParameterJdbcTemplate template;

    public SashokJdbcRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
    }

    public Long create(BaseModel baseModel) {
        String sequenceId = """
                SELECT nextval('sashok_id_seq')
                """;
        Long sashokId = template.queryForObject(sequenceId, Map.of(), Long.class);

        Map<String, Object> map = Map.of(
                "id", sashokId,
                "json_variable", baseModel.jsonValue(),
                "start_date", LocalDateTime.now());
        String sql = """
                insert into sashok(id, json_variable, start_date, status) 
                values (:id, :json_variable::JSONB, :start_date, 'ACTIVE')
                """;
        template.update(sql, map);
        return sashokId;
    }

    @Transactional
    public void error(BaseModel baseModel, Exception exception) {
        String road = JsonUtil.toJson(baseModel.road()).orElseThrow();
        Map<String, Object> sashokMap = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "road", road,
                "end_date", LocalDateTime.now());
        String sashokSql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    road = :road,
                    end_date = :end_date,
                    status = 'ERROR'
                where id = :id;
                """;
        template.update(sashokSql, sashokMap);

        Map<String, Object> errorMessageMap = Map.of(
                "sashok_id", baseModel.sashokId(),
                "message", exception.getMessage(),
                "stack_trace", Arrays.toString(exception.getStackTrace()),
                "create_date", LocalDateTime.now());
        String errorMessageSql = """
                insert into error_message(id, sashok_id, message, stack_trace, create_date)
                values (nextval('error_message_id_seq'), :sashok_id, :message, :stack_trace, :create_date);
                """;
        template.update(errorMessageSql, errorMessageMap);
    }

    public void dene(BaseModel baseModel) {
        String road = JsonUtil.toJson(baseModel.road()).orElseThrow();
        Map<String, Object> map = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "road", road,
                "end_date", LocalDateTime.now());
        String sql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    road = :road,
                    end_date = :end_date,
                    status = 'SUCCESS'
                where id = :id;
                """;
        template.update(sql, map);
    }

    public Optional<String> findRoadById(Long sashokId) {
        var sql = "SELECT road FROM sashok WHERE id = :sashokId";
        String road = template.queryForObject(sql, Map.of("sashokId", sashokId), String.class);
        return Optional.ofNullable(road);
    }
}
