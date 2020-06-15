package com.rentacar.agentbackend.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AddressDTO {

    private UUID id;

    private String city;

    private String street;

    private int number;

}
