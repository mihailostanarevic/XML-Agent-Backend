package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class GetCarBrandsFilterRequest {

    private String brandName;

    private String brandCountry;
}
