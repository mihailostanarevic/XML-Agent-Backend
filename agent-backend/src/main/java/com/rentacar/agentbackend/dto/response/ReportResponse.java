package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponse {

    private String kilometersTraveled;

    private String kilometersTraveledInTotal;

    private String agentName;

    private String brandName;

    private String modelName;

    private UUID reportId;
}
