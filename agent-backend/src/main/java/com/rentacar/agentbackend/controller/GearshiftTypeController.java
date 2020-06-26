package com.rentacar.agentbackend.controller;

import com.rentacar.agentbackend.dto.request.CreateGearshiftTypeRequest;
import com.rentacar.agentbackend.dto.request.GetGearshiftTypesWithFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateGearshiftTypeRequest;
import com.rentacar.agentbackend.dto.response.GearshiftTypeResponse;
import com.rentacar.agentbackend.service.IGearshiftTypeService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/gearshift-types")
public class GearshiftTypeController {

    private final IGearshiftTypeService _gearshiftTypeService;

    public GearshiftTypeController(IGearshiftTypeService gearshiftTypeService) {
        _gearshiftTypeService = gearshiftTypeService;
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CRUD_GEARSHIFT_TYPE')")
    public GearshiftTypeResponse createGearshiftType(@RequestBody CreateGearshiftTypeRequest request) throws Exception{
        return _gearshiftTypeService.createGearshiftType(request);
    }

    @PutMapping("/{id}/gearshift-type")
    @PreAuthorize("hasAuthority('CRUD_GEARSHIFT_TYPE')")
    public GearshiftTypeResponse updateGearshiftType(@RequestBody UpdateGearshiftTypeRequest request, @PathVariable UUID id) throws Exception{
        return _gearshiftTypeService.updateGearshiftType(request, id);
    }

    @DeleteMapping("/{id}/gearshift-type")
    @PreAuthorize("hasAuthority('CRUD_GEARSHIFT_TYPE')")
    public void deleteGearshiftType(@PathVariable UUID id) throws Exception{
        _gearshiftTypeService.deleteGearshiftType(id);
    }

    @GetMapping("/{id}/gearshift-type")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public GearshiftTypeResponse getGearshiftType(@PathVariable UUID id) throws Exception{
        return _gearshiftTypeService.getGearshiftType(id);
    }

    @GetMapping
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<GearshiftTypeResponse> getAllGearshiftTypes() throws Exception{
        return _gearshiftTypeService.getAllGearshiftTypes();
    }

    @GetMapping("/with-filter")
    @PreAuthorize("hasAuthority('VIEW_AD')")
    public List<GearshiftTypeResponse> getAllGearshiftTypesWithFilter(GetGearshiftTypesWithFilterRequest request) throws Exception{
        return _gearshiftTypeService.getAllGearshiftTypesWithFilter(request);
    }
}
