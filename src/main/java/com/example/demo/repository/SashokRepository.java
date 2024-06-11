package com.example.demo.repository;

import org.springframework.stereotype.Repository;


@Repository
public class SashokRepository {

    private static StepRepository stepRepository = null;
    private static SashokJdbcRepository sashokJdbcRepository = null;

    public SashokRepository(StepRepository stepRepository, SashokJdbcRepository sashokJdbcRepository) {
        SashokRepository.stepRepository = stepRepository;
        SashokRepository.sashokJdbcRepository = sashokJdbcRepository;
    }

    public static StepRepository step() {
        return stepRepository;
    }

    public static SashokJdbcRepository jdbc() {
        return sashokJdbcRepository;
    }
}
