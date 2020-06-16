package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.RateAdRequest;
import com.rentacar.agentbackend.dto.response.RatingResponse;

public interface IRatingService {

    RatingResponse rateAd(RateAdRequest request) throws Exception;
}
