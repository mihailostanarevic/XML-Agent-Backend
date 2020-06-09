package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDTO {

    private UUID adID;

    private boolean limitedDistance;

    private String availableKilometersPerRent;

    private int seats;

    private boolean cdw;

    private LocalDate date;

    private UUID carID;

    private String carBrand;

    private String carModel;

    private String carClass;

    private String fuelType;

    private String gearshiftType;

    private UUID agent;

    private String agentName;

    private String location;

}
