package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class CreateGearshiftTypeRequest {

    private String type;

    private String numberOfGears;
}
