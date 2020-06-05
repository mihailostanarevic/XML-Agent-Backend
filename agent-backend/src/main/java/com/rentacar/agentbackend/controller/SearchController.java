package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.response.SearchResultDTO;
import com.rentacar.agentbackend.service.ISearchService;
import com.rentacar.agentbackend.service.impl.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/light")
    public List<SearchResultDTO> search(@RequestParam(value="city") String city, @RequestParam(value="from") String from, @RequestParam(value="to") String to){
        return searchService.search(city, from, to);
    }

}
