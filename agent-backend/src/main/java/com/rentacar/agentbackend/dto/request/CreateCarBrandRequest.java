package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class CreateCarBrandRequest {

    private String name;

    private String country;
}
