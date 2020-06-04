package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CarBrandResponse {

    private UUID id;

    private String name;

    private String country;
}
