package com.example.demo.repository.postgres;

import com.example.demo.route.model.BaseModel;
import com.example.demo.route.model.BuildRouteData;
import com.example.demo.route.model.RouteData;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.example.demo.common.JsonUtil.toJsonOrElseThrow;
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
        String firstStep = baseModel.receiverName();
        String[] split = firstStep.split(":");
        String routeName = split[2];
        String sql = """
                select id
                from route
                where name = :name
                  and active_flag = true
                order by version desc
                limit 1;
                """;
        Long routeId = template.queryForObject(sql, Map.of("name", routeName), Long.class);
        if (isNull(routeId)) throw new EntityNotFoundException("Route not found with name: " + routeName);

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
        String sql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    end_date = :end_date,
                    status = 'ERROR'
                where id = :id;
                """;
        template.update(sql, sashokMap);

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
        String passedRoute = toJsonOrElseThrow(baseModel.passedRoute());
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
        String passedRoute = toJsonOrElseThrow(baseModel.passedRoute());
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

    public String findPassedRouteByIdOrElseThrow(Long sashokId) {
        String sql = """
                select passed_route
                from sashok
                where id = :sashokId
                  and status != 'SUCCESS'
                """;
        String route = template.queryForObject(sql, Map.of("sashokId", sashokId), String.class);
        if (isNull(route)) throw new EntityNotFoundException("Sashok not found with ID: " + sashokId);
        return route;
    }

    public void saveRoute(BuildRouteData buildRouteData, Integer version) {
        String createData = toJsonOrElseThrow(buildRouteData);

        Map<String, Object> insertMap = Map.of(
                "name", buildRouteData.name(),
                "create_data", createData,
                "version", version,
                "create_date", LocalDateTime.now(),
                "active_flag", true);
        String insertSql = """
                insert into route (id, name, create_data, version, create_date, active_flag)
                values (default, :name, :create_data::JSONB, :version, :create_date, :active_flag);
                """;
        template.update(insertSql, insertMap);
    }

    public List<RouteData> findActiveRoutes() {
        RowMapper<RouteData> rowMapper = (rs, rowMap) -> new RouteData(
                rs.getString("createData"),
                rs.getInt("version")
        );
        String selectSql = """
                select r.create_data as createData,
                       r.version     as version
                from route r
                where r.active_flag = true
                order by r.version desc;
                """;
        return template.query(selectSql, rowMapper);
    }

    public void cancel(BaseModel baseModel) {
        Map<String, Object> sashokMap = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "end_date", LocalDateTime.now());
        String sql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    end_date = :end_date,
                    status = 'CANCELLED'
                where id = :id;
                """;
        template.update(sql, sashokMap);
    }

    public void forbidden(BaseModel baseModel) {
        Map<String, Object> sashokMap = Map.of(
                "id", baseModel.sashokId(),
                "json_variable", baseModel.jsonValue(),
                "end_date", LocalDateTime.now());
        String sql = """
                update sashok
                set json_variable = :json_variable::JSONB,
                    end_date = :end_date,
                    status = 'FORBIDDEN'
                where id = :id;
                """;
        template.update(sql, sashokMap);
    }

    public void deactivateRoute(String name) {
        String sql = """
                update route
                set active_flag = false
                where name = :name;
                """;
        template.update(sql, Map.of("name", name));
    }

    public boolean isCancelled(BaseModel baseModel) {
        String sql = """
                select EXISTS (
                    select 1
                    from cancelled
                    where sashok_id = :sashok_id
                );
                """;
        return Boolean.TRUE.equals(template.queryForObject(sql, Map.of("sashok_id", baseModel.sashokId()), Boolean.class));
    }

    @Transactional
    public void tryCancel(Long sashokId) {
        String sql = """
                update sashok
                set status = 'TRY_CANCEL'
                where id = :id;
                """;
        template.update(sql, Map.of("id", sashokId));

        Map<String, Object> errorMessageMap = Map.of(
                "sashok_id", sashokId,
                "create_date", LocalDateTime.now());
        String errorMessageSql = """
                insert into cancelled(id, sashok_id, create_date)
                values (default, :sashok_id, :create_date);
                """;
        template.update(errorMessageSql, errorMessageMap);
    }

    public Integer findRouteLastVersion(String routeName) {
        String sql = """
                select version
                from route
                where name = :name
                order by version desc
                limit 1
                """;
        Integer version = template.queryForObject(sql, Map.of("name", routeName), Integer.class);
        if (isNull(version)) version = 1;
        return version;
    }

    public String findFirstStepOrElseThrow(String routeName) {
        String sql = """
                select r.create_data -> 'steps' -> 0 -> 'value' ->> 'name' as firstStep,
                       r.version as version
                from route r
                where r.active_flag = true
                order by r.version desc
                limit 1
                """;
        Map<String, Object> result = template.queryForObject(sql, Map.of("name", routeName), new ColumnMapRowMapper());
        if (isNull(result)) throw new EntityNotFoundException("Route not found with name: " + routeName);
        Object firstStep = result.get("firstStep");
        Object version = result.get("version");
        return "direct" + ":r:" + routeName + ":s:" + firstStep + ":v:" + version;
    }
}
