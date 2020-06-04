package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class UpdateGearshiftTypeRequest {

    private String type;

    private String numberOfGears;
}
