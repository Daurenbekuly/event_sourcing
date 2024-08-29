package com.example.demo.repository.cassandra;

import org.springframework.stereotype.Repository;

@Repository
public class CassandraRepository {

    private final StepRepository step;
    private final RetryRepository retry;

    public CassandraRepository(StepRepository step,
                               RetryRepository retry) {
        this.step = step;
        this.retry = retry;
    }

    public StepRepository step() {
        return step;
    }

    public RetryRepository retry() {
        return retry;
    }
}
