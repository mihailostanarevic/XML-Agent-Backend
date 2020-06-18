package com.rentacar.agentbackend.dto.request;

import lombok.Data;

@Data
public class CreateReportRequest {

    private String description;

    private String kilometersTraveled;
}
