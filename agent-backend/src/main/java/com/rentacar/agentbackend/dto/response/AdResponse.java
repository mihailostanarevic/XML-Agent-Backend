package com.rentacar.agentbackend.dto.response;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class AdResponse {

    private UUID id;

    private UUID agentID;

    private String name;            // ad name

    private boolean limitedDistance;

    private String availableKilometersPerRent;

    private int seats;

    private boolean cdw;

    private List<AddressDTO> fullLocations;

}
