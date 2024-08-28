package com.example.demo.repository.cassandra;

import com.example.demo.repository.cassandra.entity.RetryEntity;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface RetryRepository extends CassandraRepository<RetryEntity, UUID> {
}
