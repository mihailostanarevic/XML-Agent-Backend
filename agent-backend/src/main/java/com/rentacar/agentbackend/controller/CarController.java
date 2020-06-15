package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.AddCarAccessoriesRequest;
import com.rentacar.agentbackend.dto.request.AddKilometersRequest;
import com.rentacar.agentbackend.dto.request.CreateCarRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarRequest;
import com.rentacar.agentbackend.dto.response.CarAccessoryResponse;
import com.rentacar.agentbackend.dto.response.CarResponse;
import com.rentacar.agentbackend.service.ICarService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/cars")
public class CarController {

    private final ICarService _carService;

    public CarController(ICarService carService) {
        _carService = carService;
    }

    @PostMapping
    public CarResponse createCar(@RequestBody CreateCarRequest request) throws Exception{
        return _carService.createCar(request);
    }

    @PutMapping("/{id}/car")
    public CarResponse updateCar(@RequestBody UpdateCarRequest request, @PathVariable UUID id) throws Exception{
        return _carService.updateCar(request, id);
    }

    @DeleteMapping("/{id}/car")
    public void deleteCar(@PathVariable UUID id) throws Exception{
        _carService.deleteCar(id);
    }

    @GetMapping("/{id}/car")
    public CarResponse getCar(@PathVariable UUID id) throws Exception{
        return _carService.getCar(id);
    }

    @GetMapping
    public List<CarResponse> getAllCars() throws Exception{
        return _carService.getAllCars();
    }

    @PutMapping("/add-kilometers/{id}/car")
    public void addKilometers(@RequestBody AddKilometersRequest request, @PathVariable UUID id) throws Exception{
        _carService.addKilometers(request, id);
    }

    @PutMapping
    public void addCarAccessories(@RequestBody AddCarAccessoriesRequest request) throws Exception{
        _carService.addCarAccessories(request);
    }

    @GetMapping("/{id}")
    public List<CarAccessoryResponse> getCarAccessories(@PathVariable("id") UUID id){
        return _carService.getCarAccessories(id);
    }
}
