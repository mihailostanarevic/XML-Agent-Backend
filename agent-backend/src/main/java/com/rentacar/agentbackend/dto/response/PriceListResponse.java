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

    private String price7days;

    private String price15days;

    private String price30days;

    private UUID id;
}
