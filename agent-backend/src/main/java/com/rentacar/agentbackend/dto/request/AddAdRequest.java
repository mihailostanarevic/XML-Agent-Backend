package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
public class AddAdRequest {

    private UUID carModelId;

    private UUID gearshifTypeId;

    private UUID fuelTypeId;

    private Set<String> photoUrls;

    private UUID agentId;

    private boolean limitedDistance;

    private String availableKilometersPerRent;

    private String kilometersTraveled;

    private int seats;

    private boolean cdw;

}
