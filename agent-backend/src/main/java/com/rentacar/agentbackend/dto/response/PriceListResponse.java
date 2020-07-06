package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PriceListResponse {

    private String agentName;

    private String price1day;

    private String discount7days;

    private String discount15days;

    private String discount30days;

    private UUID id;
}
