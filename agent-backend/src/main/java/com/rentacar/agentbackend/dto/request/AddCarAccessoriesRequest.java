package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddCarAccessoriesRequest {

    private UUID carAccessoriesId;

    private UUID carId;
}
