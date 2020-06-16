package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, UUID> {

    Comment findOneById(UUID id);
}
