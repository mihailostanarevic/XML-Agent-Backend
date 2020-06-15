package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Ad;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.SimpleUser;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRequestRepository extends JpaRepository<Request, UUID> {

    Request findOneById(UUID id);

    List<Request> findAllByStatus(RequestStatus requestStatus);

}
