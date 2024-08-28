package com.example.demo.repository.cassandra.entity;

import com.example.demo.route.model.BaseModel;
import com.example.demo.common.JsonUtil;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.sql.Timestamp;

@Entity
@Table(name = "sashok")
public class SashokEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "json_variable")
    private Object jsonVariable;
    @Column(name = "route")
    private String route;
    @Column(name = "start_date")
    private Timestamp startDate;
    @Column(name = "end_date")
    private Timestamp endDate;
    @Column(name = "status")
    private String status;

    public SashokEntity() {
    }

    public SashokEntity(BaseModel baseModel, String status) {
        this.id = baseModel.sashokId();
        this.jsonVariable = baseModel.jsonValue();
        this.route = JsonUtil.toJson(baseModel.route()).orElseThrow();
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Object getJsonVariable() {
        return jsonVariable;
    }

    public void setJsonVariable(Object jsonVariable) {
        this.jsonVariable = jsonVariable;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public Timestamp getStartDate() {
        return startDate;
    }

    public void setStartDate(Timestamp startDate) {
        this.startDate = startDate;
    }

    public Timestamp getEndDate() {
        return endDate;
    }

    public void setEndDate(Timestamp endDate) {
        this.endDate = endDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
