package com.example.demo.repository.cassandra;

import com.example.demo.repository.cassandra.entity.StoppedStepEntity;
import com.example.demo.route.model.BaseModel;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoppedStepRepository extends CassandraRepository<StoppedStepEntity, UUID> {

    Boolean existsBySashokId(Long sashokId);

    default Boolean isCancelled(BaseModel baseModel) {
        return existsBySashokId(baseModel.sashokId());
    }
}
