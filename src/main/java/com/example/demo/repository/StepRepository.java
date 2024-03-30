package com.example.demo.repository;

import com.example.demo.entity.StepEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StepRepository extends CassandraRepository<StepEntity, UUID> {

    Optional<StepEntity> findByStepId(UUID uuid);
}
