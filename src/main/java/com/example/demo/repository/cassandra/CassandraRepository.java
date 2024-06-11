package com.example.demo.repository.cassandra;

import org.springframework.stereotype.Repository;

@Repository
public class CassandraRepository {

    private final StepRepository step;
    private final StoppedStepRepository stoppedStep;

    public CassandraRepository(StepRepository step, StoppedStepRepository stoppedStep) {
        this.step = step;
        this.stoppedStep = stoppedStep;
    }

    public StepRepository step() {
        return step;
    }

    public StoppedStepRepository stoppedStep() {
        return stoppedStep;
    }
}
