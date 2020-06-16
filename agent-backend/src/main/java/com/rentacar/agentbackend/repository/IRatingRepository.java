package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface IRatingRepository extends JpaRepository<Rating, UUID> {

    Rating findOneById(UUID id);

    List<Rating> findAllBySimpleUser_Id(UUID id);

    List<Rating> findAllByAd_Id(UUID id);

    Rating findOneByAd_IdAndSimpleUser_Id(UUID adId, UUID simpleUserId);
}
