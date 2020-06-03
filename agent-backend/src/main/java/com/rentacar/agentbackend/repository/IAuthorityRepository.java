package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

@SuppressWarnings("ALL")
public interface IAuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(String name);

    Authority findOneByName(String name);

    Authority findOneById(Long id);
}
