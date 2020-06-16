package com.rentacar.agentbackend.repository;

import com.rentacar.agentbackend.entity.Comment;
import com.rentacar.agentbackend.util.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ICommentRepository extends JpaRepository<Comment, UUID> {

    Comment findOneById(UUID id);

    List<Comment> findAllByAd_IdAndStatus(UUID id, RequestStatus status);

    List<Comment> findAllByStatus(RequestStatus status);
}
