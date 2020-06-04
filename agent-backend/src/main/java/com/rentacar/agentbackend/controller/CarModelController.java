package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateCarModelRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarModelRequest;
import com.rentacar.agentbackend.dto.response.CarModelResponse;
import com.rentacar.agentbackend.service.ICarModelService;
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
    public CarModelResponse createCarMode(@RequestBody CreateCarModelRequest request) throws Exception{
        return _carModelService.createCarModel(request);
    }

    @PutMapping("/{id}/car-model")
    public CarModelResponse updateCarModel(@RequestBody UpdateCarModelRequest request, @PathVariable UUID id) throws Exception{
        return _carModelService.updateCarModel(request, id);
    }

    @DeleteMapping("/{id}/car-model")
    public void deleteCarModel(@PathVariable UUID id) throws Exception{
        _carModelService.deleteCarModel(id);
    }

    @GetMapping("/{id}/car-model")
    public CarModelResponse getCarModel(@PathVariable UUID id) throws Exception{
        return _carModelService.getCarModel(id);
    }

    @GetMapping
    public List<CarModelResponse> getAllCarModels() throws Exception{
        return _carModelService.getAllCarModels();
    }
}
