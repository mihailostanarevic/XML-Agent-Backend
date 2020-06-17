package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class ApproveOrDenyCommentRequest {

    private UUID commentId;
}
