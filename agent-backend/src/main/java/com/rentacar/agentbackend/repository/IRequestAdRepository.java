package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Ad;
import com.rentacar.agentbackend.entity.Request;
import com.rentacar.agentbackend.entity.RequestAd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRequestAdRepository extends JpaRepository<RequestAd, UUID> {

    RequestAd findOneById(UUID id);

    List<RequestAd> findAllByAd(Ad ad);

    List<RequestAd> findAllByRequest(Request request);

}
