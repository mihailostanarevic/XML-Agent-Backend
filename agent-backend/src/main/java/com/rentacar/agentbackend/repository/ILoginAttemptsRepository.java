package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.LoginAttempts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ILoginAttemptsRepository extends JpaRepository<LoginAttempts, UUID> {

    LoginAttempts findOneById(UUID id);

    LoginAttempts findOneByIpAddress(String ipAddress);
}
