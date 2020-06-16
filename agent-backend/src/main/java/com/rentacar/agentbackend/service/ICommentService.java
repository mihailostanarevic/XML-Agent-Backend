package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CommentAdRequest;
import com.rentacar.agentbackend.dto.response.CommentResponse;

import java.util.List;
import java.util.UUID;

public interface ICommentService {

    CommentResponse commentAd(CommentAdRequest request) throws Exception;

    void approveComment(UUID id) throws Exception;

    void denyComment(UUID id) throws Exception;

    List<CommentResponse> getAllCommentsByAd(UUID id) throws Exception;

    List<CommentResponse> getAllPendingComments() throws Exception;
}
