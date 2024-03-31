package com.example.demo.repository;

import com.example.demo.model.BaseModel;
import com.example.demo.util.JsonUtil;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

@Repository
public class SashokJdbcRepo {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public SashokJdbcRepo(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.insert = new SimpleJdbcInsert(template.getJdbcTemplate()).withTableName("sashok").usingGeneratedKeyColumns("id");
    }

    public Long create(BaseModel baseModel) {
        Map<String, Object> map = Map.of(
                "json_variable", baseModel.jsonValue(),
                "start_date", LocalDateTime.now(),
                "status", "ACTIVE");
        Number id = insert.executeAndReturnKey(map);
        return id.longValue();
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
