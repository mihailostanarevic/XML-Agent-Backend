package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.CarBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICarBrandRepository extends JpaRepository<CarBrand, UUID> {

    CarBrand findOneById(UUID id);

    List<CarBrand> findAllByDeleted(boolean deleted);
}
