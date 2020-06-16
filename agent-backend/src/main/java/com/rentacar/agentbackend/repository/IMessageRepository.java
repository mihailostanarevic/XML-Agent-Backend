package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IMessageRepository extends JpaRepository<Message, UUID> {
    Message findOneById(UUID id);
}
