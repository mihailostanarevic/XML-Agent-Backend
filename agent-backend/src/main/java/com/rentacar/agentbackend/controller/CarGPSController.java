package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.TrackCarRequest;
import com.rentacar.agentbackend.dto.response.CarGpsResponse;
import com.rentacar.agentbackend.service.ICarGSPService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gps")
public class CarGPSController {

    private final ICarGSPService _carGSPService;

    public CarGPSController(ICarGSPService carGSPService) {
        _carGSPService = carGSPService;
    }

    @PostMapping
    public CarGpsResponse trackCar(@RequestBody TrackCarRequest request) throws Exception {
        return _carGSPService.trackCar(request);
    }

    @GetMapping("/trackable-cars/{id}/agent")
    public List<CarGpsResponse> getAllCarsWhichCanBeTrackedByAgent(@PathVariable UUID id) throws Exception {
        return _carGSPService.getAllCarsWhichCanBeTrackedByAgent(id);
    }

    @GetMapping("/tracked-cars/{id}/agent")
    public List<CarGpsResponse> getAllTrackedCarsByAgent(@PathVariable UUID id) throws Exception {
        return _carGSPService.getAllTrackedCarsByAgent(id);
    }
}
