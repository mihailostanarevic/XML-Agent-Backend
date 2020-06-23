package com.rentacar.agentbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateGearshiftTypeRequestDTO {

    private String type;

    private String numberOfGears;
}
