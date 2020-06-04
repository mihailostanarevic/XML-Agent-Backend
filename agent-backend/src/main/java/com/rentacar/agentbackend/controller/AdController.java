package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.AddAdRequest;
import com.rentacar.agentbackend.dto.request.UpdateAdRequest;
import com.rentacar.agentbackend.dto.response.AdResponse;
import com.rentacar.agentbackend.service.IAdService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/ads")
public class AdController {

    private final IAdService _adService;

    public AdController(IAdService adService) {
        _adService = adService;
    }

    @PostMapping
    public AdResponse addAd(@RequestBody AddAdRequest request) throws Exception{
        return _adService.createAd(request);
    }

    @PutMapping("/{id}/ad")
    public AdResponse updateAd(@RequestBody UpdateAdRequest request, @PathVariable UUID id) throws Exception{
        return _adService.updateAd(request, id);
    }

    @DeleteMapping("/{id}/ad")
    public void deleteAd(@PathVariable UUID id) throws Exception{
        _adService.deleteAd(id);
    }

    @GetMapping("/{id}/ad")
    public AdResponse getAd(@PathVariable UUID id) throws Exception{
        return _adService.getAd(id);
    }

    @GetMapping
    public List<AdResponse> getAllAds() throws Exception{
        return _adService.getAllAds();
    }

    @GetMapping("/{id}/car-brand")
    public List<AdResponse> getAdByCarBrand(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByCarBrand(id);
    }

    @GetMapping("/{id}/car-model")
    public List<AdResponse> getAdByCarModel(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByCarModel(id);
    }

    @GetMapping("/{id}/car-class")
    public List<AdResponse> getAdByCarClass(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByCarClass(id);
    }

    @GetMapping("/{id}/gearshift-type")
    public List<AdResponse> getAdByGearshiftType(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByGearshiftType(id);
    }

    @GetMapping("/{id}/fuel-type")
    public List<AdResponse> getAdByFuelType(@PathVariable UUID id) throws Exception{
        return _adService.getAllAdsByFuelType(id);
    }

    @GetMapping("/with-gas")
    public List<AdResponse> getAllByGas() throws Exception{
        return _adService.getAllAdsByGas();
    }
}

