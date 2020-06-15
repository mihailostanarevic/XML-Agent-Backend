package com.rentacar.agentbackend.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class SimpleUserRequests {

    private UUID id;

    private String agent;

    private String ad;

    private String receptionDate;

    private String pickUpAddress;

}
