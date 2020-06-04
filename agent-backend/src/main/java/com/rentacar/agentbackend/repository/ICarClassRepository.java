package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.CarClass;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICarClassRepository extends JpaRepository<CarClass, UUID> {

    CarClass findOneById(UUID id);

    List<CarClass> findAllByDeleted(boolean deleted);
}
