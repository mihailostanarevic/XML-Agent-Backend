package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.response.SearchResultResponse;
import com.rentacar.agentbackend.service.impl.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/light")
    public List<SearchResultResponse> search(@RequestParam(value="city") String city, @RequestParam(value="from") String from, @RequestParam(value="to") String to){
        return searchService.search(city, from, to);
    }

}
