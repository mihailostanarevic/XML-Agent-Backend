package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarGpsResponse {

    private String lat;

    private String lng;

    private String brandName;

    private String modelName;

    private String customer;

    private UUID id;

    private UUID carId;

    private UUID customerId;
}
