package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateCarBrandRequest;
import com.rentacar.agentbackend.dto.request.GetCarBrandsFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarBrandRequest;
import com.rentacar.agentbackend.dto.response.CarBrandResponse;
import com.rentacar.agentbackend.service.ICarBrandService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car-brands")
public class CarBrandController {

    private final ICarBrandService _carBrandService;

    public CarBrandController(ICarBrandService carBrandService) {
        _carBrandService = carBrandService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CRUD_CAR_BRAND')")
    public CarBrandResponse createCarBrand(@RequestBody CreateCarBrandRequest request) throws Exception{
        return _carBrandService.createCarBrand(request);
    }

    @PutMapping("/{id}/car-brand")
    @PreAuthorize("hasAuthority('CRUD_CAR_BRAND')")
    public CarBrandResponse updateCarBrand(@RequestBody UpdateCarBrandRequest request, @PathVariable UUID id) throws Exception{
        return _carBrandService.updateCarBrand(request, id);
    }

    @DeleteMapping("/{id}/car-brand")
    @PreAuthorize("hasAuthority('CRUD_CAR_BRAND')")
    public void deleteCarBrand(@PathVariable UUID id) throws Exception{
        _carBrandService.deleteCarBrand(id);
    }

    @GetMapping("/{id}/car-brand")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public CarBrandResponse getCarBrand(@PathVariable UUID id) throws Exception{
        return _carBrandService.getCarBrand(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<CarBrandResponse> getAllCarBrands() throws Exception{
        return _carBrandService.getAllCarBrands();
    }

    @GetMapping("with-filter")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<CarBrandResponse> getAllCarBrandsWithFilter(GetCarBrandsFilterRequest request) throws Exception{
        return _carBrandService.getAllCarBrandsWithFilter(request);
    }
}
