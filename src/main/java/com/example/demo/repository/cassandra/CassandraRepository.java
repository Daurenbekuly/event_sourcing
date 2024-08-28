package com.example.demo.repository.cassandra;

import org.springframework.stereotype.Repository;

@Repository
public class CassandraRepository {

    private final StepRepository step;
    private final StoppedStepRepository stoppedStep;
    private final RetryRepository retry;

    public CassandraRepository(StepRepository step,
                               StoppedStepRepository stoppedStep,
                               RetryRepository retry) {
        this.step = step;
        this.stoppedStep = stoppedStep;
        this.retry = retry;
    }

    public StepRepository step() {
        return step;
    }

    public StoppedStepRepository stoppedStep() {
        return stoppedStep;
    }

    public RetryRepository retry() {
        return retry;
    }
}
