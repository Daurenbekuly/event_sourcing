package com.example.demo.repository.cassandra;

import com.example.demo.repository.cassandra.entity.StepEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface StepRepository extends CassandraRepository<StepEntity, UUID> {

    @Query(allowFiltering = true)
    Optional<StepEntity> findFirstByStepIdAndCreateDateLessThan(UUID uuid, LocalDateTime dateTime);
}
