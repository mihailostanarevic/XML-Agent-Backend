package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@SuppressWarnings("ALL")
@Repository
public interface IAuthorityRepository extends JpaRepository<Authority, Long> {

    Authority findByName(String name);

    Authority findOneByName(String name);

    Authority findOneById(Long id);
}
