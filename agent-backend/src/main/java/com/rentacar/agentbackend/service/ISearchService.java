package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.SearchResultResponse;

import java.util.List;
import java.util.UUID;

public interface ISearchService {

    List<SearchResultResponse> search(String city, String from, String to);
    List<SearchResultResponse> advancedSearch(String city, String from, String to, String brand, String model, String fuelType, String gearshiftType, String carClass, int priceFrom, int priceTo, int estimatedDistance, boolean cdw, int childrenSeats);
}
