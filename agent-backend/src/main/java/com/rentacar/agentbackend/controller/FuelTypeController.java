package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateFuelTypeRequest;
import com.rentacar.agentbackend.dto.request.GetFuelTypesWithFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateFuelTypeRequest;
import com.rentacar.agentbackend.dto.response.FuelTypeResponse;
import com.rentacar.agentbackend.service.IFuelTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/fuel-types")
public class FuelTypeController {

    private final IFuelTypeService _fuelTypeService;

    public FuelTypeController(IFuelTypeService fuelTypeService) {
        _fuelTypeService = fuelTypeService;
    }

    @PostMapping
    public FuelTypeResponse createFuelType(@RequestBody CreateFuelTypeRequest request) throws Exception{
        return _fuelTypeService.createFuelType(request);
    }

    @PutMapping("/{id}/fuel-type")
    public FuelTypeResponse updateFuelType(@RequestBody UpdateFuelTypeRequest request, @PathVariable UUID id) throws Exception{
        return _fuelTypeService.updateFuelType(request, id);
    }

    @DeleteMapping("/{id}/fuel-type")
    public void deleteFuelType(@PathVariable UUID id) throws Exception{
        _fuelTypeService.deleteFuelType(id);
    }

    @GetMapping("/{id}/fuel-type")
    public FuelTypeResponse getFuelType(@PathVariable UUID id) throws Exception{
        return _fuelTypeService.getFuelType(id);
    }

    @GetMapping
    public List<FuelTypeResponse> getAllFuelTypes() throws Exception{
        return _fuelTypeService.getAllFuelTypes();
    }

    @GetMapping("/with-filter")
    public List<FuelTypeResponse> getAllFuelTypesWithFilter(GetFuelTypesWithFilterRequest request) throws Exception{
        return _fuelTypeService.getAllFuelTypesWithFilter(request);
    }
}
