package com.rentacar.agentbackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultResponse {

    private AdSearchResponse ad;

    private AgentSearchResponse agent;

    private CarSearchResponse car;

}
