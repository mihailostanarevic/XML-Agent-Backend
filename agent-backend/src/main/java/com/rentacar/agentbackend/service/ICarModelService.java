package com.rentacar.agentbackend.service;

import com.rentacar.agentbackend.dto.request.CreateCarModelRequest;
import com.rentacar.agentbackend.dto.request.GetCarModelsFilterRequest;
import com.rentacar.agentbackend.dto.request.UpdateCarModelRequest;
import com.rentacar.agentbackend.dto.response.CarModelResponse;

import java.util.List;
import java.util.UUID;

public interface ICarModelService {

    CarModelResponse createCarModel(CreateCarModelRequest request) throws Exception;

    CarModelResponse updateCarModel(UpdateCarModelRequest request, UUID id) throws Exception;

    void deleteCarModel(UUID id) throws Exception;

    CarModelResponse getCarModel(UUID id) throws Exception;

    List<CarModelResponse> getAllCarModels() throws Exception;

    List<CarModelResponse> getAllCarModelsWithFilter(GetCarModelsFilterRequest request) throws Exception;
}
