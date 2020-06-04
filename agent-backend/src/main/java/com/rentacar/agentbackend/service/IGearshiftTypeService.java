package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreateGearshiftTypeRequest;
import com.rentacar.agentbackend.dto.request.UpdateGearshiftTypeRequest;
import com.rentacar.agentbackend.dto.response.GearshiftTypeResponse;

import java.util.List;
import java.util.UUID;

public interface IGearshiftTypeService {

    GearshiftTypeResponse createGearshiftType(CreateGearshiftTypeRequest request) throws Exception;

    GearshiftTypeResponse updateGearshiftType(UpdateGearshiftTypeRequest request, UUID id) throws Exception;

    void deleteGearshiftType(UUID id) throws Exception;

    GearshiftTypeResponse getGearshiftType(UUID id) throws Exception;

    List<GearshiftTypeResponse> getAllGearshiftTypes() throws Exception;

}
