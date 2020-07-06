package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarSearchResponse {

    private UUID carID;

    private String carModelName;        //

    private String carBrandName;        //

    private String carClassName;        //

    private String carClassDesc;

    private String fuelTypeType;        //

    private String fuelTypeTankCapacity;        //

    private boolean fuelTypeGas;            //

    private String gearshiftTypeType;   //

    private String getGearshiftTypeNumberOfGears;       //

    private String kilometersTraveled;
}
