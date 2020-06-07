package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class GetGearshiftTypesWithFilterRequest {

    private String type;

    private String numberOfGears;
}
