package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.RateAdRequest;
import com.rentacar.agentbackend.dto.response.RatingResponse;
import com.rentacar.agentbackend.service.IRatingService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/{id}/simple-user")
    List<RatingResponse> getAllRatingsBySimpleUser(@PathVariable UUID id) throws Exception {
        return _ratingService.getAllRatingsByCustomer(id);
    }

    @GetMapping("/{id}/ad")
    List<RatingResponse> getAllRatingsByAd(@PathVariable UUID id) throws Exception {
        return _ratingService.getAllRatingsByAd(id);
    }
}
