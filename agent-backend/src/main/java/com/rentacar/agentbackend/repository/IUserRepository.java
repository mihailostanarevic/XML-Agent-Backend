package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IUserRepository extends JpaRepository<User, UUID> {

    User findOneById(UUID id);

    @PostFilter("hasPermission(returnObject, 'READ')")
    List<User> findAll();

    User findOneByUsername(String username);

    List<User> findAllByDeleted(boolean deleted);
}
