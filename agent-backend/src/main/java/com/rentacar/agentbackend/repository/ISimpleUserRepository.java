package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Agent;
import com.rentacar.agentbackend.entity.SimpleUser;
import com.rentacar.agentbackend.entity.User;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ISimpleUserRepository extends JpaRepository<SimpleUser, UUID> {

    SimpleUser findOneById(UUID id);

    List<SimpleUser> findAllByUser_Deleted(boolean deleted);

    List<Agent> findAllByRequestStatus(RequestStatus requestStatus);

    SimpleUser findOneByUser(User user);

}
