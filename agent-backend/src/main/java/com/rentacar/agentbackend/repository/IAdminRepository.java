package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IAdminRepository extends JpaRepository<Admin, UUID> {

    Admin findOneById(UUID id);

    List<Admin> findAllByUser_Deleted(boolean deleted);
}
