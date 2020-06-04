package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.CarAccessories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICarAccessoriesRepository extends JpaRepository<CarAccessories, UUID> {

    CarAccessories findOneById(UUID id);

    List<CarAccessories> findAllByDeleted(boolean deleted);
}
