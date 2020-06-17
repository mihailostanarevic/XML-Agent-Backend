package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CommentAdRequest {

    private UUID userId;

    private String comment;

    private UUID adId;
}
