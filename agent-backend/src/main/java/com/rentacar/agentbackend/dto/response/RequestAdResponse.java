package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestAdResponse {

    private String brandName;

    private String modelName;

    private LocalDate pickUpDate;

    private LocalDate returnDate;

    private UUID requestAdId;
}
