package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class GetCarModelsFilterRequest {

    private String brandName;

    private String className;
}
