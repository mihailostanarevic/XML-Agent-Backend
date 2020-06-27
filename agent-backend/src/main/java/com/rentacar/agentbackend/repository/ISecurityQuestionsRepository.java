package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.SecurityQuestions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ISecurityQuestionsRepository extends JpaRepository<SecurityQuestions, UUID> {
}
