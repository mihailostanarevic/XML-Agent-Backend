package com.rentacar.agentbackend.dto.request;

import lombok.Data;

import java.util.UUID;

@Data
public class GetIdRequest {

    private UUID id;
}
