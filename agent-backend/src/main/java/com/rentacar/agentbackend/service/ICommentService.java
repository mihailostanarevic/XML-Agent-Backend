package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.ApproveOrDenyCommentRequest;
import com.rentacar.agentbackend.dto.request.CommentAdRequest;
import com.rentacar.agentbackend.dto.response.CommentResponse;
import com.rentacar.agentbackend.service.impl.GeneralException;

import java.util.List;
import java.util.UUID;

public interface ICommentService {

    CommentResponse commentAd(CommentAdRequest request) throws Exception;

    void approveComment(ApproveOrDenyCommentRequest request) throws Exception;

    void denyComment(ApproveOrDenyCommentRequest request) throws Exception;

    List<CommentResponse> getAllCommentsByAd(UUID id) throws Exception;

    List<CommentResponse> getAllPendingComments() throws Exception;

    void checkSQLInjectionComment(CommentAdRequest request)throws GeneralException;
}
