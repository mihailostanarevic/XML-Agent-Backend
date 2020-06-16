package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CommentAdRequest;
import com.rentacar.agentbackend.dto.response.CommentResponse;

public interface ICommentService {

    CommentResponse commentAd(CommentAdRequest request) throws Exception;
}
