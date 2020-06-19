package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;
import java.util.UUID;

@Repository
public interface IAddressRepository extends JpaRepository<Address, UUID> {

    Address findOneById(UUID id);

    Set<Address> findAllByCountry(String country);
}
