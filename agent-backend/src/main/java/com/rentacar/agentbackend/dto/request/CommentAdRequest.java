package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentAdRequest {

    private UUID simpleUserId;

    private String comment;

    private UUID adId;
}
