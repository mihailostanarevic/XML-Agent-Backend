package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RateAdRequest {

    private UUID simpleUserId;

    private String grade; //rating

    private UUID adId;
}
