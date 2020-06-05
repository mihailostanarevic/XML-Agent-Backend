package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.SearchResultDTO;

import java.util.List;

public interface ISearchService {

    List<SearchResultDTO> search(String city, String from, String to);

}
