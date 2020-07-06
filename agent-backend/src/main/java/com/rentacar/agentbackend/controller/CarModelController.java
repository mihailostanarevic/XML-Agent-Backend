package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateCarModelRequest;
import com.rentacar.agentbackend.dto.request.GetCarModelsFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarModelRequest;
import com.rentacar.agentbackend.dto.response.CarModelResponse;
import com.rentacar.agentbackend.entity.CarModel;
import com.rentacar.agentbackend.service.ICarModelService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car-models")
public class CarModelController {

    private final ICarModelService _carModelService;

    public CarModelController(ICarModelService carModelService) {
        _carModelService = carModelService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('UPDATE_AD')")
    public CarModelResponse createCarModel(@RequestBody CreateCarModelRequest request) throws Exception{
        return _carModelService.createCarModel(request);
    }

    @PutMapping("/{id}/car-model")
    @PreAuthorize("hasAuthority('CRUD_CAR_MODEL')")
    public CarModelResponse updateCarModel(@RequestBody UpdateCarModelRequest request, @PathVariable UUID id) throws Exception{
        return _carModelService.updateCarModel(request, id);
    }

    @DeleteMapping("/{id}/car-model")
    @PreAuthorize("hasAuthority('CRUD_CAR_MODEL')")
    public void deleteCarModel(@PathVariable UUID id) throws Exception{
        _carModelService.deleteCarModel(id);
    }

    @GetMapping("/{id}/car-model")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public CarModelResponse getCarModel(@PathVariable UUID id) throws Exception{
        return _carModelService.getCarModel(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<CarModelResponse> getAllCarModels() throws Exception{
        return _carModelService.getAllCarModels();
    }

    @GetMapping("/with-filter")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<CarModelResponse> getAllCarModelsWithFilter(GetCarModelsFilterRequest request) throws Exception{
        return _carModelService.getAllCarModelsWithFilter(request);
    }


    @GetMapping("/car-brand/{id}")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<CarModelResponse> getCarModelsByBrand(@PathVariable("id") UUID id){
        return _carModelService.getCarModelsByBrand(id);
    }
}
