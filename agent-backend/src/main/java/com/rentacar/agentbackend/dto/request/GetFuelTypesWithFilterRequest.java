package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class GetFuelTypesWithFilterRequest {

    private String type;

    private String tankCapacity;
}
