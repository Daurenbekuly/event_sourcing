package com.example.demo.repository;

import com.example.demo.entity.StoppedStepEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoppedStepRepository extends CassandraRepository<StoppedStepEntity, UUID> {

    Boolean existsBySashokId(Long sashokId);
}
