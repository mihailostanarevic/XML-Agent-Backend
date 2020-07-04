package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreatePriceListRequest;
import com.rentacar.agentbackend.dto.request.UpdatePriceListRequest;
import com.rentacar.agentbackend.dto.response.PriceListResponse;

import java.util.List;
import java.util.UUID;

public interface IPriceListService {

    PriceListResponse createPriceList(CreatePriceListRequest request) throws Exception;

    PriceListResponse updatePriceList(UpdatePriceListRequest request) throws Exception;

    PriceListResponse getPriceList(UUID id) throws Exception;

    PriceListResponse getPriceListByAgent(UUID id) throws Exception;

    List<PriceListResponse> getAllPriceLists() throws Exception;

    void deletePriceList(UUID id) throws Exception;

    void deletePriceListByAgent(UUID id) throws Exception;
}
