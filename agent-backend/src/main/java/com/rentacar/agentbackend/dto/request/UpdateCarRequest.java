package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class UpdateCarRequest {

    private String kilometersTraveled;

    private boolean gas; //fuelType
}
