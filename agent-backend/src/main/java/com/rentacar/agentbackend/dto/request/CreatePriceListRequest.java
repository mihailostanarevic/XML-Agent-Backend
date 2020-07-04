package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatePriceListRequest {

    private UUID agentId;

    private String price1day;

    private String price7days;

    private String price15days;

    private String price30days;
}
