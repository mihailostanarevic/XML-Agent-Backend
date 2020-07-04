package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreatePriceListRequest;
import com.rentacar.agentbackend.dto.request.UpdatePriceListRequest;
import com.rentacar.agentbackend.dto.response.PriceListResponse;
import com.rentacar.agentbackend.entity.PriceList;
import com.rentacar.agentbackend.repository.IAgentRepository;
import com.rentacar.agentbackend.repository.IPriceListRepository;
import com.rentacar.agentbackend.service.IPriceListService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PriceListService implements IPriceListService {

    private final IPriceListRepository _priceListRepository;

    private final IAgentRepository _agentRepository;

    public PriceListService(IPriceListRepository priceListRepository, IAgentRepository agentRepository) {
        _priceListRepository = priceListRepository;
        _agentRepository = agentRepository;
    }

    @Override
    public PriceListResponse createPriceList(CreatePriceListRequest request) throws Exception {
        PriceList priceList = new PriceList();
        priceList.setDeleted(false);
        priceList.setAgentId(request.getAgentId());
        priceList.setPrice1day(request.getPrice1day());
        priceList.setPrice7days(request.getPrice7days());
        priceList.setPrice15days(request.getPrice15days());
        priceList.setPrice30days(request.getPrice30days());
        PriceList savedPriceList = _priceListRepository.save(priceList);
        return mapPriceListToPriceListResponse(savedPriceList);
    }

    @Override
    public PriceListResponse updatePriceList(UpdatePriceListRequest request) throws Exception {
        PriceList priceList = _priceListRepository.findOneByAgentId(request.getAgentId());
        priceList.setPrice1day(request.getPrice1day());
        priceList.setPrice7days(request.getPrice7days());
        priceList.setPrice15days(request.getPrice15days());
        priceList.setPrice30days(request.getPrice30days());
        _priceListRepository.save(priceList);
        return mapPriceListToPriceListResponse(priceList);
    }

    @Override
    public PriceListResponse getPriceList(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneById(id);
        return mapPriceListToPriceListResponse(priceList);
    }

    @Override
    public PriceListResponse getPriceListByAgent(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneByAgentId(id);
        return mapPriceListToPriceListResponse(priceList);
    }

    @Override
    public List<PriceListResponse> getAllPriceLists() throws Exception {
        List<PriceList> priceLists = _priceListRepository.findAllByDeleted(false);
        return priceLists.stream()
                .map(priceList -> mapPriceListToPriceListResponse(priceList))
                .collect(Collectors.toList());
    }

    @Override
    public void deletePriceList(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneById(id);
        priceList.setDeleted(true);
        _priceListRepository.save(priceList);
    }

    @Override
    public void deletePriceListByAgent(UUID id) throws Exception {
        PriceList priceList = _priceListRepository.findOneByAgentId(id);
        priceList.setDeleted(true);
        _priceListRepository.save(priceList);
    }

    private PriceListResponse mapPriceListToPriceListResponse(PriceList priceList) {
        PriceListResponse response = new PriceListResponse();
        response.setAgentName(_agentRepository.findOneById(priceList.getAgentId()).getName());
        response.setId(priceList.getId());
        response.setPrice1day(priceList.getPrice1day());
        response.setPrice7days(priceList.getPrice7days());
        response.setPrice15days(priceList.getPrice15days());
        response.setPrice30days(priceList.getPrice30days());
        return response;
    }
}
