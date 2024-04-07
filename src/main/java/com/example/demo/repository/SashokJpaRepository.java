package com.example.demo.repository;

import com.example.demo.entity.SashokEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SashokJpaRepository extends JpaRepository<SashokEntity, Long> {

}
