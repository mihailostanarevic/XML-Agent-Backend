package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class CreateCarClassRequest {

    private String name;

    private String description;
}
