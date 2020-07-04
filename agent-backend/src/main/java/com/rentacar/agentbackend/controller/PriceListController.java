package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreatePriceListRequest;
import com.rentacar.agentbackend.dto.request.UpdatePriceListRequest;
import com.rentacar.agentbackend.dto.response.PriceListResponse;
import com.rentacar.agentbackend.service.IPriceListService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/price-lists")
public class PriceListController {

    private final IPriceListService _priceListService;

    public PriceListController(IPriceListService priceListService) {
        _priceListService = priceListService;
    }

    @PostMapping
    public PriceListResponse createPriceList(@RequestBody CreatePriceListRequest request) throws Exception {
        return _priceListService.createPriceList(request);
    }

    @PutMapping
    public PriceListResponse updatePriceList(@RequestBody UpdatePriceListRequest request) throws Exception {
        return _priceListService.updatePriceList(request);
    }

    @GetMapping("/{id}")
    public PriceListResponse getPriceList(@PathVariable UUID id) throws Exception {
        return _priceListService.getPriceList(id);
    }

    @GetMapping("/{id}/agent")
    public PriceListResponse getPriceListByAgent(@PathVariable UUID id) throws Exception {
        return _priceListService.getPriceListByAgent(id);
    }

    @GetMapping
    public List<PriceListResponse> getAllPriceLists() throws Exception {
        return _priceListService.getAllPriceLists();
    }

    @DeleteMapping("/{id}")
    public void deletePriceList(@PathVariable UUID id) throws Exception {
        _priceListService.deletePriceList(id);
    }

    @DeleteMapping("/{id}/agent")
    public void deletePriceListByAgent(@PathVariable UUID id) throws Exception {
        _priceListService.deletePriceListByAgent(id);
    }
}
