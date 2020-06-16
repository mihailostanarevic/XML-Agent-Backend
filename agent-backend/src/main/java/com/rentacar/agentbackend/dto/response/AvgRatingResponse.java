package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AvgRatingResponse {

    private String avgRating;

    private String carBrandName;

    private String carModelName;

    private String agentEmail;

    private String agentName;
}
