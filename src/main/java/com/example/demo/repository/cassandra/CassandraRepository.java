package com.example.demo.repository.cassandra;

import com.example.demo.repository.cassandra.entity.StepEntity;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.UUID;

@Repository
public class CassandraRepository {

    private final StepRepository step;

    public CassandraRepository(StepRepository step) {
        this.step = step;
    }

    public StepRepository step() {
        return step;
    }

    public StepEntity findFirstByStepIdOrElseThrow(UUID uuid) {
        return step.findFirstByStepIdAndCreateDateLessThan(uuid, LocalDateTime.now())
                .orElseThrow(() -> new EntityNotFoundException("Step not found with params: %s".formatted(uuid)));
    }
}
