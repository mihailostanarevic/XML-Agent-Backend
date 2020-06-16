package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class RequestsSimpleUser {

    private UUID id;

    private String requestStatus;

}
