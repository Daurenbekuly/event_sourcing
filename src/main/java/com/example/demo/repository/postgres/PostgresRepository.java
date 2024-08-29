package com.example.demo.repository.postgres;

import com.example.demo.route.model.BaseModel;
import com.example.demo.route.model.BuildRouteData;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.common.JsonUtil.toJson;
import static java.util.Objects.isNull;

@Repository
public class PostgresRepository {

    private final NamedParameterJdbcTemplate template;
    private final SimpleJdbcInsert insert;

    public PostgresRepository(NamedParameterJdbcTemplate template) {
        this.template = template;
        this.insert = new SimpleJdbcInsert(template.getJdbcTemplate()).withTableName("sashok").usingGeneratedKeyColumns("id");
    }

    public Long active(BaseModel baseModel) {
        RowMapper<Long> rowMapper = (rs, rowMap) -> rs.getLong("id");
        var selectSql = "select id as id from route where first_step = :firstStep order by version desc limit 1";
        Long routeId = template.query(selectSql, Map.of("firstStep", baseModel.receiverName()), rowMapper)
                .stream()
                .findFirst()
                .orElseThrow();

        Map<String, Object> map = Map.of(
                "json_variable", baseModel.jsonValue(),
                "start_date", LocalDateTime.now(),
                "status", "ACTIVE",
                "route_id", routeId);
        Number id = insert.executeAndReturnKey(map);
        return id.longValue();
    }

    @Transactional
    public void error(BaseModel baseModel, Exception exception) {
        Map<String, Object> sashokMap = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "end_date", LocalDateTime.now());
        String sashokSql = """
                update sashok
                set json_variable = :json_variable::JSONB,
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
                values (default, :sashok_id, :message, :stack_trace, :create_date);
                """;
        template.update(errorMessageSql, errorMessageMap);
    }

    public void retry(BaseModel baseModel) {
        String passedRoute = toJson(baseModel.passedRoute()).orElseThrow();
        Map<String, Object> map = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "passed_route", passedRoute);
        String sql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    passed_route = :passed_route,
                    status = 'ON_RETRY'
                where id = :id;
                """;
        template.update(sql, map);
    }

    public void success(BaseModel baseModel) {
        String passedRoute = toJson(baseModel.passedRoute()).orElseThrow();
        Map<String, Object> map = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "passed_route", passedRoute,
                "end_date", LocalDateTime.now());
        String sql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    passed_route = :passed_route,
                    end_date = :end_date,
                    status = 'SUCCESS'
                where id = :id;
                """;
        template.update(sql, map);
    }

    public Optional<String> findPassedRouteById(Long sashokId) {
        var sql = "SELECT passed_route FROM sashok WHERE id = :sashokId";
        String route = template.queryForObject(sql, Map.of("sashokId", sashokId), String.class);
        return Optional.ofNullable(route);
    }

    public void saveRoute(BuildRouteData buildRouteData, String firstStep) {
        RowMapper<Integer> rowMapper = (rs, rowMap) -> rs.getInt("version");
        var selectSql = "select r.version as version from route r where name = :name order by version desc limit 1";
        Integer version = template.query(selectSql, Map.of("name", buildRouteData.name()), rowMapper)
                .stream()
                .findFirst()
                .orElse(0)
                + 1;
        String createData = toJson(buildRouteData).orElseThrow();

        Map<String, Object> insertMap = Map.of(
                "name", buildRouteData.name(),
                "create_data", createData,
                "version", version,
                "first_step", firstStep,
                "create_date", LocalDateTime.now());
        String insertSql = """
                insert into route (id, name, create_data, version, first_step, create_date)
                values (default, :name, :create_data::JSONB, :version, :first_step, :create_date);
                """;
        template.update(insertSql, insertMap);
    }

    public String findRouteFirstStepByName(String name) {
        var selectSql = "select first_step from route where name = :name and active_flag = true order by version desc limit 1";
        String firstStep = template.queryForObject(selectSql, Map.of("name", name), String.class);
        if (isNull(firstStep)) throw new RuntimeException();
        return firstStep;
    }

    public List<String> createDataList() {
        RowMapper<String> rowMapper = (rs, rowMap) -> rs.getString("createData");
        var selectSql = """
                select distinct on (name) r.create_data as createData
                from route r
                order by name, version desc;
                """;
        return template.query(selectSql, rowMapper);
    }

    public void cancel(BaseModel baseModel) {
        Map<String, Object> sashokMap = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "end_date", LocalDateTime.now());
        String sashokSql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    end_date = :end_date,
                    status = 'CANCELLED'
                where id = :id;
                """;
        template.update(sashokSql, sashokMap);
    }

    public void deactivateRoute(String name) {
        String sashokSql = """
                update route
                set active_flag = false
                where name = :name;
                """;
        template.update(sashokSql, Map.of("name", name));
    }

    public boolean isCancelled(BaseModel baseModel) {
        String sql = "select count(*) from sashok where id = :id and status = 'TRY_CANCEL'";
        Integer count = template.queryForObject(sql, Map.of("id", baseModel.sashokId()), Integer.class);
        return count != null && count > 0;
    }

    public void tryCancelled(Long sashokId) {
        String sashokSql = """
                update sashok
                set status = 'TRY_CANCEL'
                where id = :id;
                """;
        template.update(sashokSql, Map.of("id", sashokId));
    }
}
