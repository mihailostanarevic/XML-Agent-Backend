package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsersAdsResponse {

    private AdSearchResponse ad;

    private AgentSearchResponse agent;

    private CarSearchResponse car;

    private String dateFrom;

    private String dateTo;

    private String timeFrom;

    private String timeTo;

}
