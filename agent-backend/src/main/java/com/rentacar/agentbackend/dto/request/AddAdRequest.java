package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class AddAdRequest {

    private String carModel;

    private String gearshifType;

    private String fuelType;

//    private List<String> photoUrls;

    private UUID agentId;

    private boolean limitedDistance;

    private String availableKilometersPerRent;

    private String kilometersTraveled;

    private int seats;

    private boolean cdw;

    private boolean simpleUser;

}
