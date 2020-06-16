package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.RateAdRequest;
import com.rentacar.agentbackend.dto.response.RatingResponse;

import java.util.List;
import java.util.UUID;

public interface IRatingService {

    RatingResponse rateAd(RateAdRequest request) throws Exception;

    List<RatingResponse> getAllRatingsByCustomer(UUID id) throws Exception;

    List<RatingResponse> getAllRatingsByAd(UUID id) throws Exception;
}
