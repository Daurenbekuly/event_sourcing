package com.example.demo.repository.cassandra;

import com.example.demo.repository.cassandra.entity.StoppedStepEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoppedStepRepository extends CassandraRepository<StoppedStepEntity, UUID> {

    Boolean existsBySashokId(Long sashokId);
}
