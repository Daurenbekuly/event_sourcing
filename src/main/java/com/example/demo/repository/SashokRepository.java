package com.example.demo.repository;

import org.springframework.stereotype.Repository;


@Repository
public class SashokRepository {

    private final StepRepository stepRepository;
    private final SashokJdbcRepository sashokJdbcRepository;

    public SashokRepository(StepRepository stepRepository, SashokJdbcRepository sashokJdbcRepository) {
        this.stepRepository = stepRepository;
        this.sashokJdbcRepository = sashokJdbcRepository;
    }


    public StepRepository step() {
        return stepRepository;
    }

    public SashokJdbcRepository jdbc() {
        return sashokJdbcRepository;
    }
}
