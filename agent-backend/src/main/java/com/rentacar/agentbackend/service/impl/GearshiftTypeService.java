package com.rentacar.agentbackend.service.impl;

import com.rentacar.agentbackend.dto.request.CreateGearshiftTypeRequest;
import com.rentacar.agentbackend.dto.request.GetGearshiftTypesWithFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateGearshiftTypeRequest;
import com.rentacar.agentbackend.dto.response.GearshiftTypeResponse;
import com.rentacar.agentbackend.entity.GearshiftType;
import com.rentacar.agentbackend.repository.IGearshiftTypeRepository;
import com.rentacar.agentbackend.service.IGearshiftTypeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GearshiftTypeService implements IGearshiftTypeService {

    private final IGearshiftTypeRepository _gearshiftTypeRepository;

    public GearshiftTypeService(IGearshiftTypeRepository gearshiftTypeRepository) {
        _gearshiftTypeRepository = gearshiftTypeRepository;
    }

    @Override
    public GearshiftTypeResponse createGearshiftType(CreateGearshiftTypeRequest request) throws Exception {
        GearshiftType gearshiftType = new GearshiftType();
        gearshiftType.setDeleted(false);
        gearshiftType.setNumberOfGears(request.getNumberOfGears());
        gearshiftType.setType(request.getType());
        GearshiftType savedGearshiftType = _gearshiftTypeRepository.save(gearshiftType);
        return mapGearshiftTypeToGearshiftTypeResponse(savedGearshiftType);
    }

    @Override
    public GearshiftTypeResponse updateGearshiftType(UpdateGearshiftTypeRequest request, UUID id) throws Exception {
        GearshiftType gearshiftType = _gearshiftTypeRepository.findOneById(id);
        gearshiftType.setNumberOfGears(request.getNumberOfGears());
        gearshiftType.setType(request.getType());
        GearshiftType savedGearshiftType = _gearshiftTypeRepository.save(gearshiftType);
        return mapGearshiftTypeToGearshiftTypeResponse(savedGearshiftType);
    }

    @Override
    public void deleteGearshiftType(UUID id) throws Exception {
        GearshiftType gearshiftType = _gearshiftTypeRepository.findOneById(id);
        gearshiftType.setDeleted(true);
        _gearshiftTypeRepository.save(gearshiftType);
    }

    @Override
    public GearshiftTypeResponse getGearshiftType(UUID id) throws Exception {
        GearshiftType gearshiftType = _gearshiftTypeRepository.findOneById(id);
        return mapGearshiftTypeToGearshiftTypeResponse(gearshiftType);
    }

    @Override
    public List<GearshiftTypeResponse> getAllGearshiftTypes() throws Exception {
        List<GearshiftType> gearshiftTypes = _gearshiftTypeRepository.findAllByDeleted(false);
        return gearshiftTypes.stream()
                .map(gearshiftType -> mapGearshiftTypeToGearshiftTypeResponse(gearshiftType))
                .collect(Collectors.toList());
    }

    @Override
    public List<GearshiftTypeResponse> getAllGearshiftTypesWithFilter(GetGearshiftTypesWithFilterRequest request) throws Exception {
        List<GearshiftType> allGearshiftTypes = _gearshiftTypeRepository.findAllByDeleted(false);
        return allGearshiftTypes
                .stream()
                .filter(gearshiftType -> {
                    if(request.getType() != null) {
                        return gearshiftType.getType().toLowerCase().contains(request.getType().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .filter(gearshiftType -> {
                    if(request.getNumberOfGears() != null) {
                        return gearshiftType.getNumberOfGears().toLowerCase().contains(request.getNumberOfGears().toLowerCase());
                    } else {
                        return true;
                    }
                })
                .map(gt -> mapGearshiftTypeToGearshiftTypeResponse(gt))
                .collect(Collectors.toList());
    }

    private GearshiftTypeResponse mapGearshiftTypeToGearshiftTypeResponse(GearshiftType gearshiftType) {
        GearshiftTypeResponse response = new GearshiftTypeResponse();
        response.setId(gearshiftType.getId());
        response.setNumberOfGears(gearshiftType.getNumberOfGears());
        response.setType(gearshiftType.getType());
        return response;
    }
}
