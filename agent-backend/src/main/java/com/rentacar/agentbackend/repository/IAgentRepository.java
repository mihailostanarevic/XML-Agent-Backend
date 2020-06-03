package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Agent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAgentRepository extends JpaRepository<Agent, UUID> {

    Agent findOneById(UUID id);

    List<Agent> findAllByUser_Deleted(boolean deleted);
}
