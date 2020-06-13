package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.MessageCarAccessories;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IMessageCarAccessoriesRepository extends JpaRepository<MessageCarAccessories, UUID> {
}
