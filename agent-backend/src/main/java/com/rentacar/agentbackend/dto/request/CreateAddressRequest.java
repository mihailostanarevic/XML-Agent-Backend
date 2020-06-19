package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class CreateAddressRequest {

    private String country;

    private String city;

    private String street;

    private String number;

    private UUID agentId;
}
