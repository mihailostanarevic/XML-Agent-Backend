package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class CreateFuelTypeRequest {

    private String type;

    private String tankCapacity;

    private boolean gas;
}
