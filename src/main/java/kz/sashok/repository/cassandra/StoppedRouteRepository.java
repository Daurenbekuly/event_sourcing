package kz.sashok.repository.cassandra;

import kz.sashok.repository.cassandra.entity.StoppedRouteEntity;
import kz.sashok.route.model.BaseModel;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StoppedRouteRepository extends CassandraRepository<StoppedRouteEntity, UUID> {

    Boolean existsBySashokId(Long sashokId);

    default Boolean isCancelled(BaseModel baseModel) {
        return existsBySashokId(baseModel.sashokId());
    }
}
