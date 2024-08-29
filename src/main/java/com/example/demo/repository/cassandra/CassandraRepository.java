package com.example.demo.repository.cassandra;

import org.springframework.stereotype.Repository;

@Repository
public class CassandraRepository {

    private final StepRepository step;
    private final StoppedRouteRepository stoppedRoute;
    private final RetryRepository retry;

    public CassandraRepository(StepRepository step,
                               StoppedRouteRepository stoppedRoute,
                               RetryRepository retry) {
        this.step = step;
        this.stoppedRoute = stoppedRoute;
        this.retry = retry;
    }

    public StepRepository step() {
        return step;
    }

    public StoppedRouteRepository stoppedRoute() {
        return stoppedRoute;
    }

    public RetryRepository retry() {
        return retry;
    }
}
