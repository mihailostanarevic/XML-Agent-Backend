package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateCarRequest {

    private String kilometersTraveled;

    private UUID carModelId;

    private UUID gearshiftTypeId;

    private UUID fuelTypeId;
}
