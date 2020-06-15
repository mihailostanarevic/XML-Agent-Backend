package com.rentacar.agentbackend.dto.response;

import lombok.Data;

import java.util.UUID;

@Data
public class AgentRequests {

    private UUID id;

    private String customer;

    private String ad;

    private String receptionDate;

    private String pickUpAddress;

}
