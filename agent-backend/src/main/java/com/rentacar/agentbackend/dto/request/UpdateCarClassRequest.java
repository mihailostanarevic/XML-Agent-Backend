package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class UpdateCarClassRequest {

    private String name;

    private String description;
}
