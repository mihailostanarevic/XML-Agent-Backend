package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreatePriceListRequest {

    private UUID agentId;

    private String price1day;

    private String discount7days;

    private String discount15days;

    private String discount30days;
}
