package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.response.SearchResultResponse;
import com.rentacar.agentbackend.service.impl.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/light")
    public List<SearchResultResponse> search(@RequestParam(value="city") String city, @RequestParam(value="from") String from, @RequestParam(value="to") String to){
        return searchService.search(city, from, to);
    }

    @GetMapping("/advanced")
    public List<SearchResultResponse> advancedSearch(@RequestParam("city") String city,@RequestParam("from") String from,@RequestParam("to") String to,@RequestParam("brand") String brand, @RequestParam("model") String model, @RequestParam("fuelType") String fuelType
            , @RequestParam("gearshiftType") String gearshiftType, @RequestParam("carClass") String carClass, @RequestParam("priceFrom") int priceFrom, @RequestParam("priceTo") int priceTo
            , @RequestParam("estimatedDistance") int estimatedDistance, @RequestParam("cdw") boolean cdw, @RequestParam("childrenSeats") int childrenSeats){
        System.out.println(city + "," + from + "," + to + "," + brand + "," + model + "," + fuelType + "," + gearshiftType + "," + carClass + "," + priceFrom + "," + priceTo + "," + estimatedDistance + "," + cdw + "," + childrenSeats);
        return searchService.advancedSearch(city, from, to, brand, model, fuelType, gearshiftType, carClass, priceFrom, priceTo, estimatedDistance, cdw, childrenSeats);
    }

}
