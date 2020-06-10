package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface IPhotoRepository extends JpaRepository<Photo, UUID> {

    Photo findOneById(UUID id);

    Photo findOneByName(String name);
}
