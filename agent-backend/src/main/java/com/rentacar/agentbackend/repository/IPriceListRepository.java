package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.PriceList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IPriceListRepository extends JpaRepository<PriceList, UUID> {

    PriceList findOneById(UUID id);

    PriceList findOneByAgentId(UUID id);

    List<PriceList> findAllByDeleted(boolean deleted);
}
