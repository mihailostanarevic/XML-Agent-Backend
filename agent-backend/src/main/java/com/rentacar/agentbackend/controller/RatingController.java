package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RateAdRequest;
import com.rentacar.agentbackend.dto.response.RatingResponse;
import com.rentacar.agentbackend.service.IRatingService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ratings")
public class RatingController {

    private final IRatingService _ratingService;

    public RatingController(IRatingService ratingService) {
        _ratingService = ratingService;
    }

    @PostMapping
    RatingResponse rateAd(@RequestBody RateAdRequest request) throws Exception {
        return _ratingService.rateAd(request);
    }
}
