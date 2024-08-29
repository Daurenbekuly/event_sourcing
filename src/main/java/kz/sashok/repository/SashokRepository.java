package kz.sashok.repository;

import kz.sashok.repository.cassandra.CassandraRepository;
import kz.sashok.repository.postgres.PostgresRepository;
import org.springframework.stereotype.Repository;


@Repository
public class SashokRepository {

    private static CassandraRepository cassandraRepository = null;
    private static PostgresRepository postgresRepository = null;

    public SashokRepository(CassandraRepository cassandraRepository, PostgresRepository postgresRepository) {
        SashokRepository.cassandraRepository = cassandraRepository;
        SashokRepository.postgresRepository = postgresRepository;
    }

    public static CassandraRepository cassandra() {
        return cassandraRepository;
    }

    public static PostgresRepository postgres() {
        return postgresRepository;
    }
}
