package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.response.SearchResultResponse;

import java.util.List;

public interface ISearchService {

    List<SearchResultResponse> search(String city, String from, String to);

}
