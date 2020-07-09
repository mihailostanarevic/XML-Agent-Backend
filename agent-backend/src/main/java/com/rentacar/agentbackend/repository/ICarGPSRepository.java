package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.CarGPS;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICarGPSRepository extends JpaRepository<CarGPS, UUID> {

    List<CarGPS> findAllByDeleted(boolean deleted);

    CarGPS findOneById(UUID id);

    CarGPS findOneByCustomerId(UUID id);

    CarGPS findOneByCarId(UUID id);
}
