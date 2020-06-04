package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CarResponse {

    private UUID id;

    private String kilometersTraveled;

    private String carClassName;

    private String carClassDescription;

    private String carBrandName;

    private String carBrandCountry;

    private String carModelName;

    private String gearshiftTypeType;

    private String gearshiftTypeNumberOfGears;

    private String fuelTypeType;

    private String fuelTypeTankCapacity; //ili enum {about 50l, about 60l, about 70l, about 80l, about 90l}

    private boolean fuelTypeGas;
}
