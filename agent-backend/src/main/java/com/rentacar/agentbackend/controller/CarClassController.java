package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateCarClassRequest;
import com.rentacar.agentbackend.dto.request.GetCarClassesWithFilter;
import com.rentacar.agentbackend.dto.request.UpdateCarClassRequest;
import com.rentacar.agentbackend.dto.response.CarClassResponse;
import com.rentacar.agentbackend.service.ICarClassService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car-classes")
public class CarClassController {

    private final ICarClassService _carClassService;

    public CarClassController(ICarClassService carClassService) {
        _carClassService = carClassService;
    }

    @PostMapping
    public CarClassResponse createCarClass(@RequestBody CreateCarClassRequest request) throws Exception{
        return _carClassService.createCarClass(request);
    }

    @PutMapping("/{id}/car-class")
    public CarClassResponse updateCarClass(@RequestBody UpdateCarClassRequest request, @PathVariable UUID id) throws Exception{
        return _carClassService.updateCarClass(request, id);
    }

    @DeleteMapping("/{id}/car-class")
    public void deleteCarClass(@PathVariable UUID id) throws Exception{
        _carClassService.deleteCarClass(id);
    }

    @GetMapping("/{id}/car-class")
    public CarClassResponse getCarClasses(@PathVariable UUID id) throws Exception{
        return _carClassService.getCarClass(id);
    }

    @GetMapping
    public List<CarClassResponse> getAllCarClasses() throws Exception{
        return _carClassService.getAllCarClasses();
    }

    @GetMapping("/with-filter")
    public List<CarClassResponse> getAllCarClassesWithFilter(GetCarClassesWithFilter request) throws Exception{
        return _carClassService.getAllCarClassesWithFilter(request);
    }
}
